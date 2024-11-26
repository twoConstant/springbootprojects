package myblog.domain.article.service;

import lombok.RequiredArgsConstructor;
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
    public List<ArticleListResDto> findArticleList() {
        return articleRepository.findAll()
                .stream().map(ArticleListResDto::toDto).toList();
    }

    @Override
    @Transactional
    public ArticleResDto findArticleById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        return ArticleResDto.toDto(article);
    }

    @Override
    //FIXME JPQL로 수정
    public List<CommentListAtArticleResDto> findCommentListByArticleId(Long id) {
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
        Article article= articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.plusStarCount();
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
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.plusViewCount();
    }

}


