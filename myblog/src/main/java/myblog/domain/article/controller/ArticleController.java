package myblog.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myblog.domain.article.dto.request.ArticleCreReqDto;
import myblog.domain.article.dto.request.ArticlePutReqDto;
import myblog.domain.article.dto.response.ArticleDetailResDto;
import myblog.domain.article.dto.response.ArticleIdResDto;
import myblog.domain.article.dto.response.ArticleSummaryResDto;
import myblog.domain.article.service.ArticleService;
import myblog.domain.comment.dto.request.*;
import myblog.domain.comment.dto.response.CommentAtArticleResDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<CommentAtArticleResDto>> getCommentAtArticle(
            @PathVariable Long articleId
        ) {
        List<CommentAtArticleResDto> response = articleService.getArticleComments(articleId);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ArticleIdResDto> postArticle(
            @Valid @RequestBody ArticleCreReqDto request
            ) {
        ArticleIdResDto response = articleService.postArticle(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<HttpStatus> putArticle(
            @PathVariable Long articleId,
            @Valid @RequestBody ArticlePutReqDto request
            ) {
        articleService.putArticle(request, articleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    // 부모 댓글 생성
    @PostMapping("/{articleId}/comments")
    public ResponseEntity<HttpStatus> postComment(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentCreReqDto request
            ) {
        articleService.creComment(request, articleId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    // 부모 댓글 수정
    @PutMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> putComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentPutReqDto request
            ) {
        articleService.putComment(request, commentId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> patchComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentPatchReqDto request
            ) {
        articleService.patchComment(commentId, request);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    // 대댓글 생성
    @PostMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> creReply(
            @PathVariable Long commentId,
            @Valid @RequestBody ReplyCreReqDto request
    ) {
        articleService.creReply(request, commentId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    // 대댓글 수정
    @PutMapping("/{articleId}/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<HttpStatus> putReply(
            @PathVariable Long replyId,
            @Valid @RequestBody ReplyPutReqDto request
            ) {
        articleService.putReply(request, replyId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{articleId}/stars/increment")
    public ResponseEntity<HttpStatus> patchArticleStar(
            @PathVariable Long articleId
    ) {
        articleService.patchArticleStar(articleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);

    }

    @PatchMapping("/{articleId}/views/increment")
    public ResponseEntity<HttpStatus> patchArticleView(
            @PathVariable Long articleId
    ) {
        articleService.patchArticleView(articleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
