package com.web.repository;

import com.web.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {

    List<TrainerEntity> findAllByStatus(String status);
}
