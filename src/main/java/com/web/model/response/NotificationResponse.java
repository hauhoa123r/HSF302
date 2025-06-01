package com.web.model.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NotificationResponse {
    private Long id;
    private String title;
    private String content;
    private Timestamp sentAt;
    private boolean isRead;
    private Long userEntityId;
}
