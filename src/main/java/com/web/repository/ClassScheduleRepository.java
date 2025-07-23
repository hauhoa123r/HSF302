package com.web.repository;

import com.web.entity.ClassScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassScheduleRepository extends JpaRepository<ClassScheduleEntity, Long> {
    Optional<ClassScheduleEntity> findById(Long classEntityId);
}
