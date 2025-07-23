package com.web.converter;

import com.web.entity.NutritionPlanDetailEntity;
import com.web.model.response.NutritionPlanDetailResponse;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class NutritionPlanDetailConverter {

    public NutritionPlanDetailResponse toResponse(NutritionPlanDetailEntity entity) {
        if (entity == null) {
            return null;
        }

        NutritionPlanDetailResponse response = new NutritionPlanDetailResponse();

        if (entity.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            response.setDate(sdf.format(entity.getDate()));
        }

        response.setCalories(entity.getCalories() != null ? String.valueOf(entity.getCalories()) : null);
        response.setProtein(entity.getProtein() != null ? entity.getProtein().toString() : null);
        response.setCarb(entity.getCarbs() != null ? entity.getCarbs().toString() : null);
        response.setFat(entity.getFat() != null ? entity.getFat().toString() : null);

        response.setStatus(entity.getStatus() != null ? entity.getStatus().getStatus() : null);

        return response;
    }
}
