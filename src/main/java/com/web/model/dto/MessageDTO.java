package com.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MessageDTO {
    private Long id;
    private Long boxChatEntityId;
    private Long memberEntityId;
    private String content;
}
