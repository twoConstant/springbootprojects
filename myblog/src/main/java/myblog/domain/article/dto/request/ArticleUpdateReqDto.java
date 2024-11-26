package myblog.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateReqDto {

    @NotBlank(message = "Title can't blank")
    private String title;

    @NotBlank(message = "Writer can't blank")
    private String writer;

    @NotBlank(message = "Content can't blank")
    private String content;

    @NotBlank(message = "Password can't blank")
    private String password;

    @NotBlank(message = "Password_valid can't blank")
    private String password_valid;

}
