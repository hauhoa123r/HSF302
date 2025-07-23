package com.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MealStatus {
    ACHIEVED("Đạt"),
    NOT_ACHIEVED("Thiếu");

    private final String status;
}
