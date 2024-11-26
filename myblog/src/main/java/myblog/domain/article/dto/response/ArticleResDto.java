package myblog.domain.article.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myblog.domain.article.entity.Article;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class ArticleResDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private Long viewCount;
    private Long starCount;

    public static ArticleResDto toDto(Article article) {
        return ArticleResDto
                .builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter())
                .createdAt(article.getCreatedAt())
                .viewCount(article.getViewCount())
                .starCount(article.getStarCount())
                .build();
    }
}
