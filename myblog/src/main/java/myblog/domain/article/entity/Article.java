package myblog.domain.article.entity;

import jakarta.persistence.*;
import lombok.*;
import myblog.domain.article.dto.request.ArticlePutReqDto;
import myblog.domain.comment.entity.Comment;
import myblog.domain.user.entity.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 그냥 클래스일 뿐이다. => 역할 + 기본 생성자가 필요하다.
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Lob    // text 자료형
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "writer", nullable = false, length = 50)
    private String writer;

    @Builder.Default
    @Column(name = "star_count", nullable = false)
    private Long starCount = 0L;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 연관관계 설정(외래키, ManyToOne)
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)    // 연관관계 설정
    private Users users;

    // 연관관계 설정(객체관점, OneToMany)
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateArticleByArticlePutReqDto(ArticlePutReqDto dto) {
        // 유효성 검사도 해당 객체 안에서 수행하는 것이 자연스럽다.
        if(!this.password.equals(dto.getPassword_valid())) {
            throw new IllegalArgumentException("비밀번호 유효성검사 fail");
        }

        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.password = dto.getPassword();
        this.writer = dto.getWriter();
    }

}
