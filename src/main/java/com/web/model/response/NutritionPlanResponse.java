package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NutritionPlanResponse {
    private Long id;
    private String planDate;
    private String mealDescription;
    private String calories;
    private Long memberEntityId;

}
