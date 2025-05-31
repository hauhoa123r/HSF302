package com.web.service;

import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.service.impl.MessageServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MessageServiceIntegrationTest {

    @Autowired
    private MessageServiceImpl messageService;

    @Test
    void getMessagesByBoxChatId_success() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(1L);
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(dto);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void sendMessage_and_deleteMessage_success() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(1L);
        dto.setContent("Tin nhắn test");
        MessageResponse created = messageService.sendMessage(dto);
        Assertions.assertNotNull(created.getId());
        // Xóa
        MessageDTO delDto = new MessageDTO();
        delDto.setId(created.getId());
        messageService.deleteMessage(delDto);
        Assertions.assertThrows(Exception.class, () -> messageService.deleteMessage(delDto));
    }

    @Test
    void getMessagesByBoxChatId_nullBoxChatEntityId() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(null);
        Assertions.assertThrows(Exception.class, () -> messageService.getMessagesByBoxChatId(dto));
    }

    @Test
    void sendMessage_duplicate() {
        // Gửi tin nhắn mới
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(1L);
        dto.setContent("Tin nhắn duplicate");
        MessageResponse created = messageService.sendMessage(dto);
        // Thử gửi lại với cùng id (nếu service kiểm tra duplicate theo id)
        MessageDTO duplicateDto = new MessageDTO();
        duplicateDto.setId(created.getId());
        duplicateDto.setBoxChatEntityId(1L);
        duplicateDto.setContent("Tin nhắn duplicate");
        Assertions.assertThrows(Exception.class, () -> messageService.sendMessage(duplicateDto));
        // Xóa tin nhắn test
        MessageDTO delDto = new MessageDTO();
        delDto.setId(created.getId());
        messageService.deleteMessage(delDto);
    }

    @Test
    void deleteMessage_notFound() {
        MessageDTO dto = new MessageDTO();
        dto.setId(999999L);
        Assertions.assertThrows(Exception.class, () -> messageService.deleteMessage(dto));
    }

    @Test
    void getMessagesByBoxChatId_notFound() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(999999L);
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(dto);
        Assertions.assertTrue(result.isEmpty());
    }
}