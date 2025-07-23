package com.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalType {
    WEIGHT_LOSS("Weight lost"),
    MUSCLE_GAIN("Muscle Gain"),
    ENDURANCE("Endurance"),
    FLEXIBILITY("Flexibility"),
    STRENGTH("Strength");

    private final String type;
}
