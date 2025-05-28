package com.web.model.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class MessageResponse {
    private Long id;
    private Long boxChatId;
    private Long senderId;
    private String content;
    private Timestamp sentAt;
    private boolean isRead;
}
