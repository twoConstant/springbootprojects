package myblog.domain.member.dto.request;

import lombok.Data;

@Data
public class RequestMemberDto {
    private Long id;
    private String name;
    private Integer age;
}
