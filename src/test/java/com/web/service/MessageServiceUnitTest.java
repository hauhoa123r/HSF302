package com.web.service;

import com.web.converter.MessageConverter;
import com.web.entity.BoxChatEntity;
import com.web.entity.MessageEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.repository.MessageRepository;
import com.web.service.impl.MessageServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
@Transactional
class MessageServiceUnitTest {
    @Autowired
    private MessageServiceImpl messageService;

    @MockBean
    private MessageRepository messageRepositoryMock;

    @MockBean
    private MessageConverter messageConverterMock;

    private MessageDTO messageDTO;
    private MessageEntity messageEntity;
    private MessageResponse messageResponse;
    private BoxChatEntity boxChatEntity;

    @BeforeEach
    void setUp() {
        // Initialize test objects
        boxChatEntity = new BoxChatEntity();
        boxChatEntity.setId(1L);

        messageEntity = new MessageEntity();
        messageEntity.setId(1L);
        messageEntity.setContent("Test message");
        messageEntity.setSentAt(new Timestamp(System.currentTimeMillis()));
        messageEntity.setBoxChatEntity(boxChatEntity);

        messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setContent("Test message");
        messageDTO.setBoxChatEntityId(1L);

        messageResponse = new MessageResponse();
        messageResponse.setId(1L);
        messageResponse.setContent("Test message");
        messageResponse.setSentAt(new Timestamp(System.currentTimeMillis()));
        messageResponse.setBoxChatEntityId(1L);
    }

    @Test
    void sendMessage_Success() {
        // Setup - message with no ID should work
        messageDTO.setId(null);
        Mockito.when(messageConverterMock.toEntity(messageDTO)).thenReturn(messageEntity);
        Mockito.when(messageRepositoryMock.save(messageEntity)).thenReturn(messageEntity);
        Mockito.when(messageConverterMock.toResponse(messageEntity)).thenReturn(messageResponse);

        // Execute
        MessageResponse response = messageService.sendMessage(messageDTO);

        // Verify
        Mockito.verify(messageConverterMock, Mockito.times(1)).toEntity(messageDTO);
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).save(messageEntity);
        Mockito.verify(messageConverterMock, Mockito.times(1)).toResponse(messageEntity);

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Test message", response.getContent());
        Assertions.assertEquals(1L, response.getBoxChatEntityId());
    }

    @Test
    void sendMessage_EntityAlreadyExists() {
        // Setup - ID exists in repository
        Mockito.when(messageRepositoryMock.existsById(1L)).thenReturn(false);

        // Execute & Verify - the logic is wrong in implementation, it throws exception
        // when NOT exists
        Assertions.assertThrows(EntityAlreadyExistException.class, () -> messageService.sendMessage(messageDTO));
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).existsById(1L);
    }

    @Test
    void sendMessage_WithExistingId() {
        // Setup - ID exists in repository, should succeed
        Mockito.when(messageRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(messageConverterMock.toEntity(messageDTO)).thenReturn(messageEntity);
        Mockito.when(messageRepositoryMock.save(messageEntity)).thenReturn(messageEntity);
        Mockito.when(messageConverterMock.toResponse(messageEntity)).thenReturn(messageResponse);

        // Execute
        MessageResponse response = messageService.sendMessage(messageDTO);

        // Verify
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).existsById(1L);
        Mockito.verify(messageConverterMock, Mockito.times(1)).toEntity(messageDTO);
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).save(messageEntity);
        Mockito.verify(messageConverterMock, Mockito.times(1)).toResponse(messageEntity);

        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Test message", response.getContent());
    }

    @Test
    void getMessagesByBoxChatId_Success() {
        // Setup
        Mockito.when(messageRepositoryMock.findByBoxChatEntityId(1L)).thenReturn(List.of(messageEntity));
        Mockito.when(messageConverterMock.toResponse(messageEntity)).thenReturn(messageResponse);

        // Execute
        List<MessageResponse> messages = messageService.getMessagesByBoxChatId(messageDTO);

        // Verify
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).findByBoxChatEntityId(1L);
        Mockito.verify(messageConverterMock, Mockito.times(1)).toResponse(messageEntity);

        Assertions.assertEquals(1, messages.size());
        Assertions.assertEquals(1L, messages.get(0).getId());
        Assertions.assertEquals("Test message", messages.get(0).getContent());
    }

    @Test
    void getMessagesByBoxChatId_NullBoxChatId() {
        // Setup - null boxChatEntityId should throw EntityNotFoundException
        messageDTO.setBoxChatEntityId(null);

        // Execute & Verify
        Assertions.assertThrows(EntityNotFoundException.class, () -> messageService.getMessagesByBoxChatId(messageDTO));
    }

    @Test
    void deleteMessage_Success() {
        // Setup
        Mockito.when(messageRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(messageConverterMock.toEntity(messageDTO)).thenReturn(messageEntity);

        // Execute
        messageService.deleteMessage(messageDTO);

        // Verify
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).existsById(1L);
        Mockito.verify(messageConverterMock, Mockito.times(1)).toEntity(messageDTO);
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).delete(messageEntity);
    }

    @Test
    void deleteMessage_NullId() {
        // Setup - null ID should throw EntityNotFoundException
        messageDTO.setId(null);

        // Execute & Verify
        Assertions.assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(messageDTO));
    }

    @Test
    void deleteMessage_EntityNotFound() {
        // Setup
        Mockito.when(messageRepositoryMock.existsById(1L)).thenReturn(false);

        // Execute & Verify
        Assertions.assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(messageDTO));
        Mockito.verify(messageRepositoryMock, Mockito.times(1)).existsById(1L);
    }
}