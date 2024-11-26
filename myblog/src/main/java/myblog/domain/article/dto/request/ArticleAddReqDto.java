package myblog.domain.article.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data    // Getter, Setter, toString
@NoArgsConstructor    // 기본 생성자
@AllArgsConstructor
@Builder
public class ArticleAddReqDto {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    // 해당 항목이 넘어오지 않았을때만 guest로 매핑, "" 나 null이 넘어왛을경우 동작 x
    private String writer = "guest";

    @NotBlank(message = "content cannot be blank")
    private String content;

    @NotBlank(message = "password cannot be blank")
    private String password;

    // `null` 또는 빈 문자열(`""`)일 때 자동으로 "guest"로 설정
    @JsonSetter
    public void setWriter(String writer) {
        this.writer = (writer == null || writer.isBlank()) ? "guest" : writer;
    }

}
