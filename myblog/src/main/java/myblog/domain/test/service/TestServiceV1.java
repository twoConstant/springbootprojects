package myblog.domain.test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.domain.member.entity.Member;
import myblog.domain.member.repository.MemberRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class TestServiceV1 implements TestService{

    private final MemberRepository memberRepository;

    @Override
    public List<Member> getAllMembers() {
        log.info("getAllMember by V1");
        return memberRepository.findAll();
    }

}
