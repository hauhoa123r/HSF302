package com.web.model.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NotificationResponse {
    private Long id;
    private String title;
    private String content;
    private Timestamp sentAt;
    private boolean isRead;
    private Long userEntityId;
}
