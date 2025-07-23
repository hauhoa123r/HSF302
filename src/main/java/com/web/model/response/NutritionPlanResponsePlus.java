package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutritionPlanResponsePlus {
    private Long id;
    private String memberName;
    private String memberAge;
    private String totalTime;
    private String createdAt;
    private String calories;
    private String protein;
    private String carb;
    private String fat;
    private String fiber;
    private String water;
    private String description;
}