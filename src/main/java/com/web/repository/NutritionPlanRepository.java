package com.web.repository;

import com.web.entity.NutritionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface NutritionPlanRepository extends JpaRepository<NutritionPlanEntity, Long> {
    List<NutritionPlanEntity> findAllByMemberEntityId(Long memberEntityId);

    List<NutritionPlanEntity> findAllByMemberEntityIdAndPlanDateBetween(Long memberEntityId, Date planDateAfter, Date planDateBefore);

    List<NutritionPlanEntity> findAllByMemberEntityIdAndPlanDate(Long memberEntityId, Date planDate);
}
