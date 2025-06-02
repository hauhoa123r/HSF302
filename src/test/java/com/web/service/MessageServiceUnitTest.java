package com.web.service;

import com.web.converter.MessageConverter;
import com.web.entity.BoxChatEntity;
import com.web.entity.MemberEntity;
import com.web.entity.MessageEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.repository.MessageRepository;
import com.web.service.impl.MessageServiceImpl;
import com.web.utils.CheckFieldObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class MessageServiceUnitTest {

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private MessageConverter messageConverter;

    @MockBean
    private CheckFieldObject checkFieldObject;

    private MessageService messageService;

    private MessageDTO messageDTO;
    private MessageEntity messageEntity;
    private MessageResponse messageResponse;
    private BoxChatEntity boxChatEntity;
    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl();
        ((MessageServiceImpl) messageService).setMessageRepository(messageRepository);
        ((MessageServiceImpl) messageService).setMessageConverter(messageConverter);
        ((MessageServiceImpl) messageService).setCheckFieldObject(checkFieldObject);

        // Setup test data
        boxChatEntity = new BoxChatEntity();
        boxChatEntity.setId(1L);

        memberEntity = new MemberEntity();
        memberEntity.setId(1L);

        messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);
        messageDTO.setMemberEntityId(1L);
        messageDTO.setContent("Test message content");

        messageEntity = new MessageEntity();
        messageEntity.setId(1L);
        messageEntity.setBoxChatEntity(boxChatEntity);
        messageEntity.setMemberEntity(memberEntity);
        messageEntity.setContent("Test message content");
        messageEntity.setSentAt(new Timestamp(System.currentTimeMillis()));

        messageResponse = new MessageResponse();
        messageResponse.setId(1L);
        messageResponse.setBoxChatEntityId(1L);
        messageResponse.setMemberEntityId(1L);
        messageResponse.setContent("Test message content");
        messageResponse.setSentAt(new Timestamp(System.currentTimeMillis()));
        messageResponse.setRead(false);
    }

    @Test
    void testSendMessage_Success() {
        // Given
        when(messageRepository.existsById(anyLong())).thenReturn(false);
        when(messageConverter.toEntity(messageDTO)).thenReturn(messageEntity);
        when(messageRepository.save(any(MessageEntity.class))).thenReturn(messageEntity);
        when(messageConverter.toResponse(messageEntity)).thenReturn(messageResponse);

        // When
        MessageResponse result = messageService.sendMessage(messageDTO);

        // Then
        assertNotNull(result);
        assertEquals(messageResponse.getId(), result.getId());
        assertEquals(messageResponse.getContent(), result.getContent());
        assertEquals(messageResponse.getBoxChatEntityId(), result.getBoxChatEntityId());
        assertEquals(messageResponse.getMemberEntityId(), result.getMemberEntityId());

        verify(checkFieldObject).check(MessageDTO.class, messageDTO);
        verify(messageConverter).toEntity(messageDTO);
        verify(messageRepository).save(any(MessageEntity.class));
        verify(messageConverter).toResponse(messageEntity);

        // Verify that sentAt was set
        ArgumentCaptor<MessageEntity> entityCaptor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(messageRepository).save(entityCaptor.capture());
        assertNotNull(entityCaptor.getValue().getSentAt());
    }

    @Test
    void testSendMessage_WithExistingId_ThrowsEntityAlreadyExistException() {
        // Given
        messageDTO.setId(1L);
        when(messageRepository.existsById(1L)).thenReturn(true);

        // When & Then
        assertThrows(EntityAlreadyExistException.class, () -> messageService.sendMessage(messageDTO));

        verify(checkFieldObject).check(MessageDTO.class, messageDTO);
        verify(messageRepository).existsById(1L);
        verify(messageConverter, never()).toEntity(any());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void testGetMessagesByBoxChatId_Success() {
        // Given
        Long boxChatId = 1L;
        List<MessageEntity> messageEntities = Arrays.asList(messageEntity);
        List<MessageResponse> expectedResponses = Arrays.asList(messageResponse);

        when(messageRepository.findByBoxChatEntityId(boxChatId)).thenReturn(messageEntities);
        when(messageConverter.toResponse(messageEntity)).thenReturn(messageResponse);

        // When
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(boxChatId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedResponses.get(0).getId(), result.get(0).getId());
        assertEquals(expectedResponses.get(0).getContent(), result.get(0).getContent());

        verify(messageRepository).findByBoxChatEntityId(boxChatId);
        verify(messageConverter).toResponse(messageEntity);
    }

    @Test
    void testGetMessagesByBoxChatId_EmptyList() {
        // Given
        Long boxChatId = 1L;
        when(messageRepository.findByBoxChatEntityId(boxChatId)).thenReturn(Collections.emptyList());

        // When
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(boxChatId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(messageRepository).findByBoxChatEntityId(boxChatId);
        verify(messageConverter, never()).toResponse(any());
    }

    @Test
    void testGetMessagesByBoxChatId_MultipleMessages() {
        // Given
        Long boxChatId = 1L;
        MessageEntity message2 = new MessageEntity();
        message2.setId(2L);
        message2.setContent("Second message");

        MessageResponse response2 = new MessageResponse();
        response2.setId(2L);
        response2.setContent("Second message");

        List<MessageEntity> messageEntities = Arrays.asList(messageEntity, message2);

        when(messageRepository.findByBoxChatEntityId(boxChatId)).thenReturn(messageEntities);
        when(messageConverter.toResponse(messageEntity)).thenReturn(messageResponse);
        when(messageConverter.toResponse(message2)).thenReturn(response2);

        // When
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(boxChatId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(messageResponse.getId(), result.get(0).getId());
        assertEquals(response2.getId(), result.get(1).getId());

        verify(messageRepository).findByBoxChatEntityId(boxChatId);
        verify(messageConverter).toResponse(messageEntity);
        verify(messageConverter).toResponse(message2);
    }

    @Test
    void testDeleteMessage_Success() {
        // Given
        Long messageId = 1L;
        when(messageRepository.existsById(messageId)).thenReturn(true);

        // When
        messageService.deleteMessage(messageId);

        // Then
        verify(messageRepository).existsById(messageId);
        verify(messageRepository).deleteById(messageId);
    }

    @Test
    void testDeleteMessage_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long messageId = 1L;
        when(messageRepository.existsById(messageId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(messageId));

        verify(messageRepository).existsById(messageId);
        verify(messageRepository, never()).deleteById(any());
    }

    @Test
    void testSendMessage_WithNullId_Success() {
        // Given
        messageDTO.setId(null);
        when(messageConverter.toEntity(messageDTO)).thenReturn(messageEntity);
        when(messageRepository.save(any(MessageEntity.class))).thenReturn(messageEntity);
        when(messageConverter.toResponse(messageEntity)).thenReturn(messageResponse);

        // When
        MessageResponse result = messageService.sendMessage(messageDTO);

        // Then
        assertNotNull(result);
        assertEquals(messageResponse.getId(), result.getId());

        verify(checkFieldObject).check(MessageDTO.class, messageDTO);
        verify(messageRepository, never()).existsById(any());
        verify(messageConverter).toEntity(messageDTO);
        verify(messageRepository).save(any(MessageEntity.class));
        verify(messageConverter).toResponse(messageEntity);
    }

    @Test
    void testSendMessage_TimestampSetCorrectly() {
        // Given
        long beforeTime = System.currentTimeMillis();
        when(messageConverter.toEntity(messageDTO)).thenReturn(messageEntity);
        when(messageRepository.save(any(MessageEntity.class))).thenReturn(messageEntity);
        when(messageConverter.toResponse(messageEntity)).thenReturn(messageResponse);

        // When
        messageService.sendMessage(messageDTO);
        long afterTime = System.currentTimeMillis();

        // Then
        ArgumentCaptor<MessageEntity> entityCaptor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(messageRepository).save(entityCaptor.capture());

        MessageEntity savedEntity = entityCaptor.getValue();
        assertNotNull(savedEntity.getSentAt());
        assertTrue(savedEntity.getSentAt().getTime() >= beforeTime);
        assertTrue(savedEntity.getSentAt().getTime() <= afterTime);
    }
}
