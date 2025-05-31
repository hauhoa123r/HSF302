package com.web.service.impl;

import com.web.entity.ClassEntity;
import com.web.entity.MemberEntity;
import com.web.repository.ClassEnrollmentRepository;
import com.web.repository.ClassRepository;
import com.web.repository.MemberRepository;
import com.web.service.ClassEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {
    @Autowired
    private ClassRepository classRepositoryImpl;

    @Autowired
    private MemberRepository memberRepositoryImpl;

    @Autowired
    private ClassEnrollmentRepository classEnrollmentRepositoryImpl;

    @Override
    public void enrollClass(Long classId, Long memberId) {
        ClassEntity classEntity = classRepositoryImpl.findById(classId).orElseThrow(() -> new RuntimeException("Class not found with id: " + classId));

        MemberEntity memberEntity = memberRepositoryImpl.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));

        Timestamp enrollAt = new Timestamp(System.currentTimeMillis());



    }

    @Override
    public void unenrollClass(Long classId, Long memberId) {

    }
}
