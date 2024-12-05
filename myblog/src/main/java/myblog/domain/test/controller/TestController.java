//package myblog.domain.test.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import myblog.domain.member.entity.Member;
//import myblog.domain.test.dto.response.TestUserDto;
//import myblog.domain.test.service.TestService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/tests")
//@RequiredArgsConstructor
//@Slf4j
//public class TestController implements TestDocs {
//
//    private final TestService testService;
//
//    @GetMapping()
//    public List<Member> getAllMembers() {
//        List<Member> members = testService.getAllMembers();
//        return members;
//    }
//
//    @GetMapping("/mono/users/1")
//    public Mono<String> getUserInfoByMono() {
//        return testService.getUserInfoByMono();
//    }
//
//    @GetMapping("/dto/users/1")
//    public ResponseEntity<TestUserDto> getUserInfoByDto() throws JsonProcessingException {
//        return ResponseEntity.ok(testService.getUserInfoByDto());
//    }
//
//}
