package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.NutritionPlanEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NutritionPlanConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public NutritionPlanEntity toEntity(NutritionPlanDTO nutritionPlanDTO) {
        Optional<NutritionPlanEntity> nutritionPlanEntityOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(nutritionPlanDTO, NutritionPlanEntity.class));

        return nutritionPlanEntityOptional.orElseThrow(() -> new ErrorMappingException(NutritionPlanDTO.class, NutritionPlanEntity.class));
    }

    public NutritionPlanResponse toResponse(NutritionPlanEntity nutritionPlanEntity) {
        Optional<NutritionPlanResponse> nutritionPlanResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(nutritionPlanEntity, NutritionPlanResponse.class));

        return nutritionPlanResponseOptional.orElseThrow(() -> new ErrorMappingException(NutritionPlanEntity.class, NutritionPlanResponse.class));
    }
}
