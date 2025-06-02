package com.web.service;

import java.sql.Date;
import java.util.List;

import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;

public interface NutritionPlanService {

    NutritionPlanResponse createNutritionPlan(NutritionPlanDTO nutritionPlanDTO);

    NutritionPlanResponse getNutritionPlanById(Long id);

    List<NutritionPlanResponse> getAllByMemberId(Long memberId);

    List<NutritionPlanResponse> getAllByMemberIdAndDate(Long memberId, Date date);

    List<NutritionPlanResponse> getAllByMemberIdAndWeek(Long memberId, Date date);

    List<NutritionPlanResponse> getAllByMemberIdAndMonth(Long memberId, Date date);

    NutritionPlanResponse updateNutritionPlan(Long id, NutritionPlanDTO nutritionPlanDTO);

    void deleteNutritionPlan(Long id);
}
