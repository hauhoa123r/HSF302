package com.web.converter;

import com.web.config.ModelMapperConfig;
import com.web.entity.NutritionPlanEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;
import com.web.model.response.NutritionPlanResponsePlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Component
public class NutritionPlanConverter {

    private ModelMapperConfig modelMapperConfig;

    @Autowired
    public void setModelMapperConfig(ModelMapperConfig modelMapperConfig) {
        this.modelMapperConfig = modelMapperConfig;
    }

    public NutritionPlanEntity toEntity(NutritionPlanDTO dto) {
        if (dto == null) return null;

        NutritionPlanEntity entity = new NutritionPlanEntity();

        entity.setTotalTime(dto.getTotalTime());
        entity.setDescription(dto.getDescription());

        if (dto.getCalories() != null) {
            entity.setCalories(dto.getCalories().longValue());
        } else {
            entity.setCalories(0L);
        }

        entity.setProtein(dto.getProtein());
        entity.setCarbs(dto.getCarb());
        entity.setFat(dto.getFat());
        entity.setFiber(dto.getFiber());
        entity.setWater(dto.getWater());

        // Set ngày tạo nếu muốn
        entity.setCreated_at(new java.sql.Date(System.currentTimeMillis()));

        return entity;
    }

    public NutritionPlanResponse toResponse(NutritionPlanEntity nutritionPlanEntity) {
        Optional<NutritionPlanResponse> nutritionPlanResponseOptional = Optional.ofNullable(modelMapperConfig.modelMapper().map(nutritionPlanEntity, NutritionPlanResponse.class));

        return nutritionPlanResponseOptional.orElseThrow(() -> new ErrorMappingException(NutritionPlanEntity.class, NutritionPlanResponse.class));
    }

    public NutritionPlanResponsePlus toResponsePlus(NutritionPlanEntity nutritionPlanEntity) {
        NutritionPlanResponsePlus nutritionPlanResponsePlus = new NutritionPlanResponsePlus();

        nutritionPlanResponsePlus.setId(nutritionPlanEntity.getId());
        nutritionPlanResponsePlus.setMemberName(nutritionPlanEntity.getMemberEntity().getUserEntity().getFullName());
        nutritionPlanResponsePlus.setMemberAge(calculateAge(nutritionPlanEntity.getMemberEntity().getUserEntity().getDateOfBirth()));
        nutritionPlanResponsePlus.setTotalTime(String.valueOf(nutritionPlanEntity.getTotalTime()));
        nutritionPlanResponsePlus.setDescription(nutritionPlanEntity.getDescription());
        nutritionPlanResponsePlus.setCalories(String.valueOf(nutritionPlanEntity.getCalories()));
        nutritionPlanResponsePlus.setProtein(String.valueOf(nutritionPlanEntity.getProtein()));
        nutritionPlanResponsePlus.setFat(String.valueOf(nutritionPlanEntity.getFat()));
        nutritionPlanResponsePlus.setFiber(String.valueOf(nutritionPlanEntity.getFiber()));
        nutritionPlanResponsePlus.setWater(String.valueOf(nutritionPlanEntity.getWater()));
        nutritionPlanResponsePlus.setCarb(String.valueOf(nutritionPlanEntity.getCarbs()));
        nutritionPlanResponsePlus.setCreatedAt(String.valueOf(nutritionPlanEntity.getCreated_at()));

        return nutritionPlanResponsePlus;
    }

    public String calculateAge(Date date){
        LocalDate birthDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        int age = today.getYear() - birthDate.getYear();

        if (today.getMonthValue() < birthDate.getMonthValue() ||
                (today.getMonthValue() == birthDate.getMonthValue() && today.getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        return String.valueOf(age);
    }
}
