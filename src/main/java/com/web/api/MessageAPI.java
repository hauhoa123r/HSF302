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

    @GetMapping("/getAll")
    public List<MessageResponse> getAllMessages(@RequestBody MessageDTO messageDTO) {
        return messageService.getMessagesByBoxChatId(messageDTO);
    }

    @PostMapping("/send")
    public MessageResponse sendMessage(@RequestBody MessageDTO messageDTO) {
        return messageService.sendMessage(messageDTO);
    }

    @PostMapping("/delete")
    public void deleteMessage(@RequestBody MessageDTO messageDTO) {
        messageService.deleteMessage(messageDTO);
    }
}
