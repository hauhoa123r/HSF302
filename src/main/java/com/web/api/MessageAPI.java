package com.web.api;

import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class  MessageAPI {

    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/box-chat/{boxChatId}")
    public List<MessageResponse> getAllMessages(@PathVariable Long boxChatId) {
        return messageService.getMessagesByBoxChatId(boxChatId);
    }

    @PostMapping()
    public MessageResponse sendMessage(@RequestBody MessageDTO messageDTO) {
        return messageService.sendMessage(messageDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }
}
