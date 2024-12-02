package myblog.domain.article.service;

import myblog.domain.article.dto.request.ArticleAddReqDto;
import myblog.domain.article.dto.request.ArticleUpdateReqDto;
import myblog.domain.article.dto.response.ArticleResDto;
import myblog.domain.article.dto.response.ArticleIdResDto;
import myblog.domain.article.dto.response.ArticleListResDto;
import myblog.domain.comment.dto.request.*;
import myblog.domain.comment.dto.response.CommentListAtArticleResDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArticleService {

    List<ArticleListResDto> findArticleList();

    ArticleResDto findArticleById(Long id);

    List<CommentListAtArticleResDto> findCommentListByArticleId(Long id);

    ArticleIdResDto addArticle(ArticleAddReqDto request);

    void updateArticle(ArticleUpdateReqDto request, Long id);

    void addComment(CommentAddReqDto request, Long articleId);

    void updateComment(CommentUpdateReqDto request, Long commentId);

    void addReply(ReplyAddReqDto request, Long commentId);

    void updateReply(ReplyUpdateReqDto request, Long replyId);

    void incrementArticleStar(Long articleId);

    void bufferedIncrementArticleStar(Long articleId);

    void modifyComment(Long commentId, CommentModifyReqDto request);

    void incrementArticleView(Long articleId);

    Page<ArticleResDto> findArticlePage(int page, int size);
}
