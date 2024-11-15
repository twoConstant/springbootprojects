package myblog.domain.article.service;

import myblog.domain.article.dto.response.ArticleDetailResDto;
import myblog.domain.article.dto.response.ArticleSummaryResDto;
import myblog.domain.comment.dto.response.CommentAtArticleReqDto;

import java.util.List;

public interface ArticleService {

    List<ArticleSummaryResDto> getArticleSummary();

    ArticleDetailResDto getArticleDetail(Long id);

    List<CommentAtArticleReqDto> getArticleComments(Long id);
}
