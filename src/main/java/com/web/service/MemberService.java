package com.web.service;

import com.web.model.dto.MemberDTO;
import com.web.model.response.MemberResponse;
import org.springframework.data.domain.Page;

public interface MemberService {

    Page<MemberResponse> getMembers(int pageIndex, int pageSize, MemberDTO memberDTO);

    MemberResponse getMember(Long id);

    void createMember(MemberDTO memberDTO);

    void updateMember(Long id, MemberDTO memberDTO);

    void deleteMember(Long id);

    Long countMembers();

}
