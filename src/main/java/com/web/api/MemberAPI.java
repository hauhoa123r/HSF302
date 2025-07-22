package com.web.api;

import com.web.model.dto.MemberDTO;
import com.web.model.response.MemberResponse;
import com.web.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MemberAPI {
    private final int PAGE_SIZE = 10;
    private MemberService memberService;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/api/admin/member/page/{pageIndex}")
    public Map<String, Object> getMembersPage(@PathVariable int pageIndex, @ModelAttribute MemberDTO memberDTO) {
        Page<MemberResponse> memberResponsePage = memberService.getMembers(pageIndex, PAGE_SIZE, memberDTO);
        return Map.of(
                "members", memberResponsePage.getContent(),
                "currentPage", memberResponsePage.getNumber(),
                "totalPages", memberResponsePage.getTotalPages()
        );
    }

    @PostMapping("/api/admin/member")
    public void addMember(@Valid @RequestBody MemberDTO memberDTO) {
        memberService.createMember(memberDTO);
    }

    @PutMapping("/api/admin/member/{memberId}")
    public void updateMember(@PathVariable Long memberId, @Valid @RequestBody MemberDTO memberDTO) {
        memberService.updateMember(memberId, memberDTO);
    }
}
