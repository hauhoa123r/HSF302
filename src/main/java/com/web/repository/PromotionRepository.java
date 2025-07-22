package com.web.repository;

import com.web.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Long>, JpaSpecificationExecutor<PromotionEntity> {
    boolean existsByIdAndStatus(Long id, String status);
}
