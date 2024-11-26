package myblog.domain.article.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myblog.domain.article.dto.request.ArticleAddReqDto;
import myblog.domain.article.dto.request.ArticleUpdateReqDto;
import myblog.domain.article.dto.response.ArticleIdResDto;
import myblog.domain.article.dto.response.ArticleListResDto;
import myblog.domain.article.dto.response.ArticleResDto;
import myblog.domain.article.service.ArticleService;
import myblog.domain.comment.dto.request.*;
import myblog.domain.comment.dto.response.CommentListAtArticleResDto;
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
    public ResponseEntity<List<ArticleListResDto>> articleList() {
        List<ArticleListResDto> response = articleService.findArticleList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResDto> articleDetail(@PathVariable Long articleId) {
        ArticleResDto response = articleService.findArticleById(articleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentListAtArticleResDto>> commentList(
            @PathVariable Long articleId
        ) {
        List<CommentListAtArticleResDto> response = articleService.findCommentListByArticleId(articleId);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ArticleIdResDto> articleAdd(
            @Valid @RequestBody ArticleAddReqDto request
            ) {
        ArticleIdResDto response = articleService.addArticle(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<HttpStatus> articleUpdate(
            @PathVariable Long articleId,
            @Valid @RequestBody ArticleUpdateReqDto request
            ) {
        articleService.updateArticle(request, articleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    // 부모 댓글 생성
    @PostMapping("/{articleId}/comments")
    public ResponseEntity<HttpStatus> commentAdd(
            @PathVariable Long articleId,
            @Valid @RequestBody CommentAddReqDto request
            ) {
        articleService.addComment(request, articleId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    // 부모 댓글 수정
    @PutMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> commentUpdate(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateReqDto request
            ) {
        articleService.updateComment(request, commentId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> commentModify(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentModifyReqDto request
            ) {
        articleService.modifyComment(commentId, request);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    // 대댓글 생성
    @PostMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> replyAdd(
            @PathVariable Long commentId,
            @Valid @RequestBody ReplyAddReqDto request
    ) {
        articleService.addReply(request, commentId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    // 대댓글 수정
    @PutMapping("/{articleId}/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<HttpStatus> replyUpdate(
            @PathVariable Long replyId,
            @Valid @RequestBody ReplyUpdateReqDto request
            ) {
        articleService.updateReply(request, replyId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{articleId}/stars/increment")
    public ResponseEntity<HttpStatus> articleStarIncrement(
            @PathVariable Long articleId
    ) {
        articleService.incrementArticleStar(articleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);

    }

    @PatchMapping("/{articleId}/views/increment")
    public ResponseEntity<HttpStatus> articleViewIncrement(
            @PathVariable Long articleId
    ) {
        articleService.incrementArticleView(articleId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
