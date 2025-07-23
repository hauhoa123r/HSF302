package com.web.enums;

public enum ClassStatus {
    ACTIVE("Đang hoạt động"),
    INACTIVE("Tạm dừng"),
    CLOSED("Đã kết thúc");

    private final String displayName;

    ClassStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
