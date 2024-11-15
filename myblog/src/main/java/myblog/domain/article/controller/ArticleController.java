package myblog.domain.article.controller;

import lombok.RequiredArgsConstructor;
import myblog.domain.article.dto.response.ArticleDetailResDto;
import myblog.domain.article.dto.response.ArticleSummaryResDto;
import myblog.domain.article.service.ArticleService;
import myblog.domain.comment.dto.response.CommentAtArticleReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping()
    public ResponseEntity<List<ArticleSummaryResDto>> getArticleSummary() {
        List<ArticleSummaryResDto> response = articleService.getArticleSummary();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDetailResDto> getArticleDetail(
            @PathVariable Long articleId
        ) {
        ArticleDetailResDto response = articleService.getArticleDetail(articleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentAtArticleReqDto>> getCommentAtArticle(
            @PathVariable Long articleId
        ) {
        List<CommentAtArticleReqDto> response = articleService.getArticleComments(articleId);
        return ResponseEntity.ok(response);
    }
}
