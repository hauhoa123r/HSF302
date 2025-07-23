package com.web.repository;

import com.web.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrainerRepository extends JpaRepository<TrainerEntity, Long>, JpaSpecificationExecutor<TrainerEntity> {

    List<TrainerEntity> findAllByStatus(String status);

    @Query("SELECT DISTINCT t.specialization FROM TrainerEntity t")
    List<String> findAllSpecializations();

    boolean existsByUserEntityEmail(String userEntityEmail);

    boolean existsByUserEntityPhone(String userEntityPhone);

    boolean existsByUserEntityIdCard(String userEntityIdCard);
}
