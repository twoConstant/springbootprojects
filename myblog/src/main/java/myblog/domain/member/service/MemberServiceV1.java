package myblog.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.domain.member.dto.request.RequestMemberDto;
import myblog.domain.member.dto.response.MemberDto;
import myblog.domain.member.entity.Member;
import myblog.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV1 implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public List<MemberDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberDto::new)
                .toList();
    }

    @Override
    public MemberDto getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .age(member.getAge())
                .build();
    }

    @Override
    public void createMember(RequestMemberDto request) {
        memberRepository.save(
                Member
                .builder()
                .name(request.getName())
                .age(request.getAge())
                .build()
                );
    }

    @Override
    public void updateMemberPartial(Long id, RequestMemberDto request) {
        log.info("service in");
        Member member = memberRepository.findById(id).orElseThrow();

        if(request.getName() != null) {
            member.setName(request.getName());
        }

        if(request.getAge() != null) {
            member.setAge(request.getAge());
        }

        log.info("service out");

        memberRepository.save(member);
    }

    @Override
    public void updateMember(Long id, RequestMemberDto request) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.setName(request.getName());
        member.setAge(request.getAge());
        memberRepository.save(member);
    }

    @Override
    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public void deleteAllMembers() {
        memberRepository.deleteAll();
    }
}
