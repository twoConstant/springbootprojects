package myblog.domain.article.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myblog.domain.article.entity.Article;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ArticleListResDto {

    private Long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;
    private Long viewCount;
    private Long starCount;

    public static ArticleListResDto toDto(Article article) {
        return ArticleListResDto
                .builder()
                .id(article.getId())
                .title(article.getTitle())
                .writer(article.getWriter())
                .viewCount(article.getViewCount())
                .starCount(article.getStarCount())
                .createdAt(article.getCreatedAt())
                .build();
    }

}
