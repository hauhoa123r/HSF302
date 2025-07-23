package com.web.repository;

import com.web.entity.WorkoutGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutGoalRepository extends JpaRepository<WorkoutGoalEntity, Long> {
}
