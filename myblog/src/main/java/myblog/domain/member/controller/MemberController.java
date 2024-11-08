package myblog.domain.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.domain.member.dto.request.RequestMemberDto;
import myblog.domain.member.dto.response.MemberDto;
import myblog.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // API 용입니다.
@RequiredArgsConstructor   // 의존성 주입
@RequestMapping("/members")    // 공통 url
@Slf4j    // log용
public class MemberController {

    private final MemberService memberService;

    // 멤버 전체 조회
    @GetMapping()
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        List<MemberDto> allMembers = memberService.getAllMembers();
        return ResponseEntity.ok(allMembers);
    }

    // 멤버 단건 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long memberId) {
        MemberDto response = memberService.getMemberById(memberId);
        return ResponseEntity.ok(response);
    }

    // 멤버 새로 추가
    @PostMapping()
    public ResponseEntity<Void> createMember(@RequestBody RequestMemberDto request) {
        memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 멤버 일부 수정
    @PatchMapping("/{memberId}")
    public ResponseEntity<Void> patchMember(
            @PathVariable Long memberId,
            @RequestBody RequestMemberDto request
    ){
        log.info("controller");
        memberService.updateMemberPartial(memberId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // 멤버 전체 수정
    @PutMapping("/{memberId}")
    public ResponseEntity<Void> putMember(
            @PathVariable Long memberId,
            @RequestBody RequestMemberDto request
    ){
        memberService.updateMember(memberId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // 멤버 단건 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMemberById(memberId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    // 멤버 전체 삭제
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllMember() {
        memberService.deleteAllMembers();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
