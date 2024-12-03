package myblog.domain.article.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.domain.article.dto.request.ArticleAddReqDto;
import myblog.domain.article.dto.request.ArticleUpdateReqDto;
import myblog.domain.article.dto.response.ArticleIdResDto;
import myblog.domain.article.dto.response.ArticleListResDto;
import myblog.domain.article.dto.response.ArticleResDto;
import myblog.domain.article.entity.Article;
import myblog.domain.article.repository.ArticleRepository;
import myblog.domain.comment.dto.request.*;
import myblog.domain.comment.dto.response.CommentListAtArticleResDto;
import myblog.domain.comment.entity.Comment;
import myblog.domain.comment.repository.CommentRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class ArticleServiceV2 implements ArticleService{

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CacheManager cacheManager;
    private final ConcurrentMap<Long, Long> starCountBuffer = new ConcurrentHashMap<>();
    @Override
    public List<ArticleListResDto> findArticleList() {
        return null;
    }

    @Override
    @Cacheable(value = "articles", key = "#id")
    @Transactional
    public ArticleResDto findArticleById(Long id) {
        log.info("findArticleById called for ID: {}", id);
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Article not found with ID: " + id));
        return ArticleResDto.toDto(article);
    }

    @Override
    @Cacheable(value = "commentsAtArticle", key = "#id")
    public List<CommentListAtArticleResDto> findCommentListByArticleId(Long id) {
        log.info("findCommentListByArticleId from DB");
        List<CommentListAtArticleResDto> response = new ArrayList<>();
        for (Comment parentComment : commentRepository.findByArticle_IdAndParentCommentIsNull(id)) {
            List<Comment> childComments = parentComment.getChildComments();
            response.add(CommentListAtArticleResDto.toDto(parentComment, childComments));
        }
        return response;
    }

    @Override
    @Transactional
    @CacheEvict(value = "articlePages", key = "'1'")
    public ArticleIdResDto addArticle(ArticleAddReqDto request) {
        log.info("Adding new article");
        Article article = articleRepository.save(
                Article.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .writer(request.getWriter())
                        .password(request.getPassword())
                        .build()
        );

        // 캐시 무효화 후 새롭게 저장될 첫 페이지 캐시 갱신
        refreshFirstPageCache();

        return ArticleIdResDto.builder()
                .id(article.getId())
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(value = "articlePages", key = "'1'")
    public void updateArticle(ArticleUpdateReqDto request, Long id) {
        log.info("Updating article with ID: {}", id);

        // Article 엔티티 업데이트
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("None content with summit id"));
        article.updateArticleByArticlePutReqDto(request);

        // ArticleResDto 생성 및 캐시 업데이트
        Cache cache = cacheManager.getCache("articles");
        if (cache != null) {
            ArticleResDto updatedArticleDto = ArticleResDto.toDto(article);
            cache.put(id, updatedArticleDto); // 캐시에 갱신된 DTO 저장
            log.info("Article cache updated for ID: {}", id);
        }

        // 첫 페이지 캐시 갱신
        refreshFirstPageCache();
    }

    @Override
    public void addComment(CommentAddReqDto request, Long articleId) {

    }

    @Override
    public void updateComment(CommentUpdateReqDto request, Long commentId) {

    }

    @Override
    public void addReply(ReplyAddReqDto request, Long commentId) {

    }

    @Override
    public void updateReply(ReplyUpdateReqDto request, Long replyId) {

    }

    @Override
    public void incrementArticleStar(Long articleId) {
        log.info("Start bufferedIncrementArticleStar");

        // 캐시 업데이트
        Cache cache = cacheManager.getCache("articles");
        if (cache != null) {
            ArticleResDto cachedArticle = cache.get(articleId, ArticleResDto.class);
            if (cachedArticle != null) {
                cachedArticle.setStarCount(cachedArticle.getStarCount() + 1); // 캐시의 추천 수 증가
                cache.put(articleId, cachedArticle); // 캐시에 업데이트된 값 저장
            }
        }

        // 쓰기 지연 버퍼에 추천 수 누적
        starCountBuffer.merge(articleId, 1L, Long::sum);

        // 버퍼 값이 10 이상이면 DB 업데이트
        if (starCountBuffer.get(articleId) >= 10) {
            flushBufferedStarCountToDatabase(articleId);
        }
    }

    private void flushBufferedStarCountToDatabase(Long articleId) {
        Long starsToAdd = starCountBuffer.remove(articleId); // 버퍼에서 값 가져오기
        if (starsToAdd != null && starsToAdd > 0) {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new IllegalArgumentException("Article Not Found"));

            log.info("Flushing buffered stars to DB. Current DB stars: {}, starsToAdd: {}", article.getStarCount(), starsToAdd);

            // DB 값 업데이트
            article.addToStarCount(starsToAdd);
            articleRepository.save(article);
        }
    }


    @Override
    public void modifyComment(Long commentId, CommentModifyReqDto request) {

    }

    @Override
    public void incrementArticleView(Long articleId) {
        log.info("incrementArticleView from DB");
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.plusViewCount();
    }

    @Override
    public Page<ArticleResDto> findArticlePage(int page, int size) {
        log.info("findArticlePage called for page: {} and size: {}", page, size);

        // 첫 페이지 요청 시 캐시 확인
        if (page == 1) {
            Cache cache = cacheManager.getCache("articlePages");
            if (cache != null) {
                @SuppressWarnings("unchecked")
                Page<ArticleResDto> cachedPage = cache.get("1", Page.class);
                if (cachedPage != null) {
                    log.info("Returning cached first page");
                    return cachedPage; // 캐시된 첫 페이지 반환
                }

                // 캐시에 데이터가 없으면 DB에서 조회 후 캐시에 저장
                Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());
                Page<Article> articlePage = articleRepository.findAll(pageable);
                if (articlePage.isEmpty()) {
                    throw new IllegalArgumentException("No page with your request");
                }
                Page<ArticleResDto> mappedPage = articlePage.map(ArticleResDto::toDto);
                cache.put("1", mappedPage); // 첫 페이지 캐시에 저장
                return mappedPage;
            }
        }

        // 첫 페이지 외의 요청은 항상 DB에서 조회
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<Article> articlePage = articleRepository.findAll(pageable);
        if (articlePage.isEmpty()) {
            throw new IllegalArgumentException("No page with your request");
        }
        return articlePage.map(ArticleResDto::toDto);
    }

    // 캐시 갱신 메서드
    private void refreshFirstPageCache() {
        Cache cache = cacheManager.getCache("articlePages");
        if (cache != null) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<Article> articlePage = articleRepository.findAll(pageable);
            Page<ArticleResDto> mappedPage = articlePage.map(ArticleResDto::toDto);
            cache.put("0", mappedPage); // 첫 페이지 갱신
            log.info("First page cache refreshed");
        }
    }
}
