package myblog.domain.article.service;

import myblog.domain.article.dto.request.ArticleCreReqDto;
import myblog.domain.article.dto.request.ArticlePutReqDto;
import myblog.domain.article.dto.response.ArticleDetailResDto;
import myblog.domain.article.dto.response.ArticleIdResDto;
import myblog.domain.article.dto.response.ArticleSummaryResDto;
import myblog.domain.comment.dto.request.*;
import myblog.domain.comment.dto.response.CommentAtArticleResDto;

import java.util.List;

public interface ArticleService {

    List<ArticleSummaryResDto> getArticleSummary();

    ArticleDetailResDto getArticleDetail(Long id);

    List<CommentAtArticleResDto> getArticleComments(Long id);

    ArticleIdResDto postArticle(ArticleCreReqDto request);

    void putArticle(ArticlePutReqDto request, Long id);

    void creComment(CommentCreReqDto request, Long articleId);

    void putComment(CommentPutReqDto request, Long commentId);

    void creReply(ReplyCreReqDto request, Long commentId);

    void putReply(ReplyPutReqDto request, Long replyId);

    void patchArticleStar(Long articleId);

    void patchComment(Long commentId, CommentPatchReqDto request);

    void patchArticleView(Long articleId);
}
