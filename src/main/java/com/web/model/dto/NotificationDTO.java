package com.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NotificationDTO {
    private Long id;
    private String title;
    private String content;
    private Timestamp sentAt;
    private boolean isRead;
    private Long userEntityId;
}
