package com.web.repository;

import com.web.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByClassNameContaining(String name);
    Page<ClassEntity> findAll(Pageable pageable);
    Page<ClassEntity> findByClassNameContainingIgnoreCase(String className, Pageable pageable);
}
