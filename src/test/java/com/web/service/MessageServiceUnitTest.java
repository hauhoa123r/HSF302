package com.web.service;

import com.web.entity.MessageEntity;
import com.web.exception.ResourceNotFoundException;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.repository.MessageRepository;
import com.web.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class MessageServiceUnitTest {
    @Autowired
    private MessageServiceImpl messageService;
    @MockBean
    private MessageRepository messageRepositoryMock;

    // --- getMessagesByBoxChatId ---
    @Test
    void getMessagesByBoxChatId_success() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(1L);
        MessageEntity entity = new MessageEntity();
        entity.setId(1L);
        Mockito.when(messageRepositoryMock.findByBoxChatEntityId(1L)).thenReturn(List.of(entity));
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(dto);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getMessagesByBoxChatId_nullBoxChatEntityId() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> messageService.getMessagesByBoxChatId(dto));
    }

    @Test
    void getMessagesByBoxChatId_notFound() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(999L);
        Mockito.when(messageRepositoryMock.findByBoxChatEntityId(999L)).thenReturn(List.of());
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(dto);
        Assertions.assertTrue(result.isEmpty());
    }

    // --- sendMessage ---
    @Test
    void sendMessage_success() {
        MessageDTO dto = new MessageDTO();
        dto.setId(1L);
        dto.setBoxChatEntityId(1L);
        dto.setMemberEntityId(1L);
        dto.setContent("Hello");
        MessageEntity entity = new MessageEntity();
        entity.setId(1L);
        Mockito.when(messageRepositoryMock.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(messageRepositoryMock.save(Mockito.any(MessageEntity.class))).thenReturn(entity);
        MessageResponse res = messageService.sendMessage(dto);
        Assertions.assertEquals(1L, res.getId());
    }

    @Test
    void sendMessage_duplicate() {
        MessageDTO dto = new MessageDTO();
        dto.setId(2L);
        dto.setBoxChatEntityId(1L);
        dto.setMemberEntityId(1L);
        dto.setContent("Hello");
        Mockito.when(messageRepositoryMock.existsById(2L)).thenReturn(true);
        Assertions.assertThrows(EntityAlreadyExistException.class, () -> messageService.sendMessage(dto));
    }

    @Test
    void sendMessage_nullContent() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(1L);
        dto.setMemberEntityId(1L);
        dto.setContent(null);
        Assertions.assertThrows(Exception.class, () -> messageService.sendMessage(dto));
    }

    @Test
    void sendMessage_nullBoxChatEntityId() {
        MessageDTO dto = new MessageDTO();
        dto.setBoxChatEntityId(null);
        dto.setMemberEntityId(1L);
        dto.setContent("Hello");
        Assertions.assertThrows(Exception.class, () -> messageService.sendMessage(dto));
    }

    // --- deleteMessage ---
    @Test
    void deleteMessage_success() {
        MessageDTO dto = new MessageDTO();
        dto.setId(1L);
        MessageEntity entity = new MessageEntity();
        entity.setId(1L);
        Mockito.when(messageRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(messageRepositoryMock.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.doNothing().when(messageRepositoryMock).deleteById(1L);
        messageService.deleteMessage(dto);
        Mockito.verify(messageRepositoryMock).delete(Mockito.any(MessageEntity.class));
    }

    @Test
    void deleteMessage_nullId() {
        MessageDTO dto = new MessageDTO();
        dto.setId(null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> messageService.deleteMessage(dto));
    }

    @Test
    void deleteMessage_notFound() {
        MessageDTO dto = new MessageDTO();
        dto.setId(999L);
        Mockito.when(messageRepositoryMock.existsById(999L)).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(dto));
    }
}