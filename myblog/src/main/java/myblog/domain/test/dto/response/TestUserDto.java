package myblog.domain.test.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestUserDto {

    private String name;
    private String userName;
    private String email;

}
