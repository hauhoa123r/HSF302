package com.web.repository;

import com.web.entity.ClassEnrollmentEntity;
import com.web.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollmentEntity, Long> {

    ClassEnrollmentEntity findByClassEntityIdAndMemberEntityId(Long classEntityId, Long memberEntityId);

    int countByClassEntity(ClassEntity classEntity);
}
