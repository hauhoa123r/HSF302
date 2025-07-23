package com.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalStatus {
    ACTIVE ("Active"),
    COMPLETED("Completed"),
    CANCELLED ("Cancelled");

    private final String status;
}
