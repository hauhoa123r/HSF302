package com.web.model.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
