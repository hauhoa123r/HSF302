package com.web.repository;

import com.web.entity.ClassEntity;
import com.web.entity.EquipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    Page<EquipmentEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<EquipmentEntity> findAll(Pageable pageable);
}
