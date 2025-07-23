package com.web.service;

import java.sql.Date;
import java.util.List;

import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.MemberOfTrainerResponse;
import com.web.model.response.NutritionPlanDetailResponse;
import com.web.model.response.NutritionPlanResponse;
import com.web.model.response.NutritionPlanResponsePlus;


public interface NutritionPlanService {
    List<MemberOfTrainerResponse> getMemberOfTrainer(Long id);

    boolean saveNutritionPlan(NutritionPlanDTO dto);

    List<NutritionPlanResponsePlus> getNutritionPlanByTrainerId(Long id);

    NutritionPlanResponsePlus getNutritionPlanById(Long id);

    List<NutritionPlanDetailResponse> getNutritionPlanDetailByPlanId(Long id);
}
