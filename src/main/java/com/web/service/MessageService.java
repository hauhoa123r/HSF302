package com.web.service;

import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageDTO messageDTO);

    List<MessageResponse> getMessagesByBoxChatId(Long boxChatId);

    void deleteMessage(Long id);

}
