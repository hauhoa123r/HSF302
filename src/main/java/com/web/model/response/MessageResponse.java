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
    private Long boxChatEntityId;
    private Long memberEntityId;
    private String content;
    private Timestamp sentAt;
    private boolean isRead;
}
