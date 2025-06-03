package com.web.service;

import com.web.entity.MemberEntity;

public interface MemberService {
    MemberEntity getMemberById(Long id);

    void saveMember(MemberEntity memberEntity);

    void updateMember(MemberEntity memberEntity);

    void deleteMember(Long id);
}
