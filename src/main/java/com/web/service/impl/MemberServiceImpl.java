package com.web.service.impl;

import com.web.entity.MemberEntity;
import com.web.exception.sql.EntityNotFoundException;
import com.web.repository.MemberRepository;
import com.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public MemberEntity getMemberById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MemberEntity.class));
    }

    @Override
    public void saveMember(MemberEntity memberEntity) {
        if (memberEntity == null) {
            throw new IllegalArgumentException("Member entity cannot be null");
        }
        memberRepository.save(memberEntity);
    }

    @Override
    public void updateMember(MemberEntity memberEntity) {
        if (memberEntity == null) {
            throw new IllegalArgumentException("Member entity cannot be null");
        }
        if (memberEntity.getId() == null) {
            throw new IllegalArgumentException("Member ID cannot be null for update");
        }
        if (!memberRepository.existsById(memberEntity.getId())) {
            throw new EntityNotFoundException(MemberEntity.class);
        }
        memberRepository.save(memberEntity);
    }

    @Override
    public void deleteMember(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        if (!memberRepository.existsById(id)) {
            throw new EntityNotFoundException(MemberEntity.class);
        }
        memberRepository.deleteById(id);
    }
}
