package com.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityLevel {
    SEDENTARY("Ít vận động"),
    LIGHT("Vận động nhẹ"),
    MODERATE("Vận động vừa"),
    ACTIVE("Vận động nhiều"),
    VERY_ACTIVES ("Vận động rất nhiều");

    private final String level;
}
