package com.web.repository;

import com.web.entity.NutritionPlanDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NutritionPlanDetailRepository extends JpaRepository<NutritionPlanDetailEntity, Long> {
    List<NutritionPlanDetailEntity> getTop5ByNutritionPlan_Id(Long id);
}
