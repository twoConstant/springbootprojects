package myblog.domain.member.service;

import myblog.domain.member.dto.request.RequestMemberDto;
import myblog.domain.member.dto.response.MemberDto;
import myblog.domain.member.entity.Member;

import java.util.List;

public interface MemberService {

    // 멤버 전체 조회
    List<MemberDto> getAllMembers();

    // 멤버 단건 조회 (ID로 조회)
    MemberDto getMemberById(Long id);

    // 멤버 새로 추가
    void createMember(RequestMemberDto request);

    // 멤버 일부 수정 (부분 업데이트)
    void updateMemberPartial(Long id, RequestMemberDto request);

    // 멤버 전체 수정 (전체 업데이트)
    void updateMember(Long id, RequestMemberDto request);

    // 멤버 단건 삭제
    void deleteMemberById(Long id);

    // 멤버 전체 삭제
    void deleteAllMembers();
}
