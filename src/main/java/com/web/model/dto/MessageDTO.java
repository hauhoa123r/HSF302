package com.web.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MessageDTO {
    private Long id;
    private Long boxChatEntityId;
    private Long memberEntityId;
    private String content;
}
