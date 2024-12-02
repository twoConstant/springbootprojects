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
import org.springframework.cache.annotation.Cacheable;
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
public class ArticleServiceV1 implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CacheManager cacheManager;
    private final ConcurrentMap<Long, Long> starCountBuffer = new ConcurrentHashMap<>();

    @Override
    @Cacheable(value = "articleList", key = "'allArticles'")
    public List<ArticleListResDto> findArticleList() {
        System.out.println("Fetching article list from database...");
        return articleRepository.findAllWithUsers()
                .stream().map(ArticleListResDto::toDto).toList();
    }

    @Override
    @Cacheable(value = "articlePages", key = "#page + '-' + #size")
    public Page<ArticleResDto> findArticlePage(int page, int size) {
        log.info("findArticlePage from DB");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Article> articlePage = articleRepository.findAll(pageable);
        if(articlePage.isEmpty()) {
            throw new IllegalArgumentException("no page with your request");
        }
        return articlePage.map(ArticleResDto::toDto);
    }

    @Override
    @Transactional
    @Cacheable(value = "articles", key = "#id")
    public ArticleResDto findArticleById(Long id) {
        log.info("findArticleById from DB");
        Article article = articleRepository.findById(id).orElseThrow();
        return ArticleResDto.toDto(article);
    }

    @Override
    @Cacheable(value = "commentsAtArticle", key = "#id")
    //FIXME JPQL로 수정
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
    //FIXME 이것도 캡슐화를 해야하는가?
    @Transactional
    public ArticleIdResDto addArticle(ArticleAddReqDto request) {
        Article article = articleRepository.save(
                Article
                        .builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .writer(request.getWriter())
                        .password(request.getPassword())
                        .build()
        );
        return ArticleIdResDto.builder()
                .id(article.getId())
                .build();
    }

    @Override
    @Transactional
    public void updateArticle(ArticleUpdateReqDto request, Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("None content with summit id"));
        article.updateArticleByArticlePutReqDto(request);
        // save 는 필요없음 자동으로 JPA가 더티체킹
    }

    @Override
    @Transactional
    public void addComment(CommentAddReqDto request, Long articleId) {
        commentRepository.save(
                Comment.creComment(
                        request,
                        articleRepository.findById(articleId).orElseThrow()
                )
        );
    }

    @Override
    @Transactional
    public void updateComment(CommentUpdateReqDto request, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.updateCommentByCommentPutReqDto(request);
    }



    @Override
    @Transactional
    public void addReply(ReplyAddReqDto request, Long commentId) {
        commentRepository.save(
                Comment.creReply(
                        request,
                        commentRepository.findById(commentId).orElseThrow()
                )
        );
    }

    @Override
    @Transactional
    public void updateReply(ReplyUpdateReqDto request, Long replyId) {
        Comment comment = commentRepository.findById(replyId).orElseThrow();
        comment.putReply(request);
    }

    @Override
    @Transactional
    public void incrementArticleStar(Long articleId) {
        log.info("incrementArticleStar from DB");
        Article article= articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.plusStarCount();
    }

    @Override
    @Transactional
    public void bufferedIncrementArticleStar(Long articleId) {
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
    @Transactional
    public void modifyComment(Long commentId, CommentModifyReqDto request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(IllegalArgumentException::new);
        comment.updateCommentByCommentPatchReqDto(request);
    }

    @Override
    @Transactional
    public void incrementArticleView(Long articleId) {
        log.info("incrementArticleView from DB");
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.plusViewCount();
    }



}


