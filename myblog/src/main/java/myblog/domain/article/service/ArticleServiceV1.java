package myblog.domain.article.service;

import lombok.RequiredArgsConstructor;
import myblog.domain.article.dto.request.ArticleCreReqDto;
import myblog.domain.article.dto.request.ArticlePutReqDto;
import myblog.domain.article.dto.response.ArticleDetailResDto;
import myblog.domain.article.dto.response.ArticleIdResDto;
import myblog.domain.article.dto.response.ArticleSummaryResDto;
import myblog.domain.article.entity.Article;
import myblog.domain.article.repository.ArticleRepository;
import myblog.domain.comment.dto.request.CommentCreReqDto;
import myblog.domain.comment.dto.request.CommentPutReqDto;
import myblog.domain.comment.dto.request.ReplyCreReqDto;
import myblog.domain.comment.dto.request.ReplyPutReqDto;
import myblog.domain.comment.dto.response.CommentAtArticleResDto;
import myblog.domain.comment.entity.Comment;
import myblog.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ArticleServiceV1 implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ArticleSummaryResDto> getArticleSummary() {
        return articleRepository.findAll()
                .stream().map(ArticleSummaryResDto::toDto).toList();
    }

    @Override
    @Transactional
    public ArticleDetailResDto getArticleDetail(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        article.plusViewCount();
        return ArticleDetailResDto.toDto(article);
    }

    @Override
    //FIXME JPQL로 수정
    public List<CommentAtArticleResDto> getArticleComments(Long id) {
        List<CommentAtArticleResDto> response = new ArrayList<>();
        for (Comment parentComment : commentRepository.findByArticle_IdAndParentCommentIsNull(id)) {
            List<Comment> childComments = parentComment.getChildComments();
            response.add(CommentAtArticleResDto.toDto(parentComment, childComments));
        }
        return response;
    }

    @Override
    //FIXME 이것도 캡슐화를 해야하는가?
    @Transactional
    public ArticleIdResDto postArticle(ArticleCreReqDto request) {
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
    public void putArticle(ArticlePutReqDto request, Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("None content with summit id"));
        article.updateArticleByArticlePutReqDto(request);
        // save 는 필요없음 자동으로 JPA가 더티체킹
    }

    @Override
    @Transactional
    public void creComment(CommentCreReqDto request, Long articleId) {
        commentRepository.save(
                Comment.creComment(
                        request,
                        articleRepository.findById(articleId).orElseThrow()
                )
        );
    }

    @Override
    @Transactional
    public void putComment(CommentPutReqDto request, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.updateCommentByCommentPutReqDto(request);
    }

    @Override
    @Transactional
    public void creReply(ReplyCreReqDto request, Long commentId) {
        commentRepository.save(
                Comment.creReply(
                        request,
                        commentRepository.findById(commentId).orElseThrow()
                )
        );
    }

    @Override
    @Transactional
    public void putReply(ReplyPutReqDto request, Long replyId) {
        Comment comment = commentRepository.findById(replyId).orElseThrow();
        comment.putReply(request);
    }

}


