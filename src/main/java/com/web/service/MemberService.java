package com.web.service;

import com.web.entity.MemberEntity;
import com.web.entity.UserEntity;

public interface MemberService {
    MemberEntity getMemberById(Long id);

    void saveMember(UserEntity userEntity);

    void updateMember(MemberEntity memberEntity);

    void deleteMember(Long id);
}
