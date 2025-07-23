package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutritionPlanDetailResponse {
    private String date;
    private String calories;
    private String protein;
    private String carb;
    private String fat;
    private String status;
}
