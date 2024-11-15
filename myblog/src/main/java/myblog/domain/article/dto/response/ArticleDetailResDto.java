package myblog.domain.article.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myblog.domain.article.entity.Article;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class ArticleDetailResDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private Long viewCount;
    private Long startCount;

    public static ArticleDetailResDto toDto(Article article) {
        return ArticleDetailResDto
                .builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .writer(article.getWriter())
                .createdAt(article.getCreatedAt())
                .viewCount(article.getViewCount())
                .startCount(article.getStarCount())
                .build();
    }
}
