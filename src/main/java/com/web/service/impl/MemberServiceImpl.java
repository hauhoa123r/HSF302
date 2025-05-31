package com.web.service.impl;

import com.web.entity.MemberEntity;
import com.web.entity.UserEntity;
import com.web.exception.sql.QRCodeGenerationException;
import com.web.repository.MemberRepository;
import com.web.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;

import java.util.UUID;

public class MemberServiceImpl implements MemberService {

    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    private MemberRepository memberRepositoryImpl;

    @Override
    public MemberEntity getMemberById(Long id) {
        return memberRepositoryImpl.findById(id).orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    @Override
    public void saveMember(UserEntity userEntity) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setUserEntity(userEntity);

            try {
                saveMemberInNewTransaction(memberEntity);
                return;
            } catch (DataIntegrityViolationException e) {
                attempt++;
            }
        }
        throw new QRCodeGenerationException("Failed to generate unique QR code after " + MAX_ATTEMPTS + " attempts");
    }

    @Transactional
    public void saveMemberInNewTransaction(MemberEntity memberEntity) {
        memberRepositoryImpl.save(memberEntity);
    }
    @Override
    public void updateMember(MemberEntity memberEntity) {
    }

    @Override
    public void deleteMember(Long id) {
        MemberEntity memberEntity = memberRepositoryImpl.findById(id).orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        memberRepositoryImpl.delete(memberEntity);
    }
}
