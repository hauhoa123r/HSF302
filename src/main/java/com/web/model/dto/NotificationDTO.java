package com.web.model.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NotificationDTO {
    private Long id;
    private String title;
    private String content;
    private Timestamp sentAt;
    private boolean isRead;
    private Long userEntityId;
}
