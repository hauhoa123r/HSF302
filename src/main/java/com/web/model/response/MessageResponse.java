package com.web.model.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
