package myblog.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreReqDto {

    @NotBlank(message = "content can not be blank")
    private String content;

    private String writer = "guest";

    @JsonSetter
    public void setWriter(String writer) {
        this.writer = (writer == null || writer.isBlank()) ? "guest" : writer;
    }
}
