package myblog.domain.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import myblog.domain.member.entity.Member;
import myblog.domain.test.dto.response.TestUserDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TestService {

    List<Member> getAllMembers();

    Mono<String> getUserInfoByMono();

    TestUserDto getUserInfoByDto() throws JsonProcessingException;


}
