package myblog.domain.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.domain.member.entity.Member;
import myblog.domain.member.repository.MemberRepository;
import myblog.domain.test.dto.request.TestUserDtoReq;
import myblog.domain.test.dto.response.TestUserDto;
import myblog.global.configure.WebClientConfig;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class TestServiceV1 implements TestService{

    // Https API 호출 객체
    private final WebClient webClient;

    // Object mapper
    private final ObjectMapper objectMapper;

    private final MemberRepository memberRepository;

    @Override
    public List<Member> getAllMembers() {
        log.info("getAllMember by V1");
        return memberRepository.findAll();
    }


    @Override
    public Mono<String> getUserInfoByMono() {
        return webClient.get()
                .uri("/users/1")
                .retrieve()
                .bodyToMono(String.class);
    }

    @Override
    public TestUserDto getUserInfoByDto() throws JsonProcessingException {
        String responseJson = webClient.get()
                .uri("/users/1")
                .retrieve()
                .bodyToMono(String.class)
                .block();// 동기화 방식으로 처리

        // 위의 결과물을 활용해 TestUserDtoReq를 생성
        TestUserDtoReq testUserDtoReq = objectMapper.readValue(responseJson, TestUserDtoReq.class);

        // TestUserDto를 생성하여 반환
        return TestUserDto.builder()
                .name(testUserDtoReq.getName())
                .userName(testUserDtoReq.getUsername())
                .email(testUserDtoReq.getEmail())
                .build();
    }

}
