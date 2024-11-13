package myblog.domain.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import myblog.domain.member.entity.Member;
import myblog.domain.test.dto.response.TestUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name ="TestDocs", description = "테스트용 API ")
public interface TestDocs {

    @GetMapping()
    @Operation(summary = "자식폴더 생성 요청", description = "정보를 반환")
    @ApiResponse(responseCode = "201", description = "폴더 생성에 성공하였습니다.")
    List<Member> getAllMembers();

    @GetMapping("/mono/users/1")
    @Operation(summary = "자식폴더 생성 요청", description = "정보를 반환")
    @ApiResponse(responseCode = "201", description = "폴더 생성에 성공하였습니다.")
    Mono<String> getUserInfoByMono();

    @GetMapping("/dto/users/1")
    @Operation(summary = "자식폴더 생성 요청", description = "정보를 반환")
    @ApiResponse(responseCode = "201", description = "폴더 생성에 성공하였습니다.")
    ResponseEntity<TestUserDto> getUserInfoByDto() throws JsonProcessingException;



}
