package com.web.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NutritionPlanDTO {
    private Long id;
    private Date planDate;
    private String mealDescription;
    private String calories;
    private Long memberEntityId;
}
