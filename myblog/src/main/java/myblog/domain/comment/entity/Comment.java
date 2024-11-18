package myblog.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import myblog.domain.article.entity.Article;
import myblog.domain.comment.dto.request.CommentCreReqDto;
import myblog.domain.comment.dto.request.CommentPutReqDto;
import myblog.domain.comment.dto.request.ReplyCreReqDto;
import myblog.domain.comment.dto.request.ReplyPutReqDto;
import myblog.domain.user.entity.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "writer", nullable = false)
    private String writer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    // 연관관계 설정(외래키, ManyToOne)
    @JoinColumn(name = "article_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @JoinColumn(name = "parent_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;

    // 객체관점
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Comment 관련 메서드
    public static Comment creComment(CommentCreReqDto request, Article article) {
        return Comment.builder()
                .content(request.getContent())
                .writer(request.getWriter())
                .article(article)
                .build();
    }

    public void updateCommentByCommentPutReqDto(CommentPutReqDto dto) {
        this.writer = dto.getWriter();
        this.content = dto.getContent();
    }

    // Reply 관련 메서드
    public static Comment creReply(ReplyCreReqDto dto, Comment comment) {
        return Comment.builder()
                .writer(dto.getWriter())
                .content(dto.getContent())
                .parentComment(comment)
                .build();
    }

    public void putReply(ReplyPutReqDto dto) {
        this.writer = dto.getWriter();
        this.content = dto.getContent();
    }
}
