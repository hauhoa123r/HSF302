package com.web.service;

import com.web.entity.MessageEntity;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MessageServiceIntegrationTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void testSendMessage_Success() {
        // Given
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);
        messageDTO.setMemberEntityId(1L);
        messageDTO.setContent("Test integration message");

        // When
        MessageResponse result = messageService.sendMessage(messageDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test integration message", result.getContent());
        assertEquals(1L, result.getBoxChatEntityId());
        assertEquals(1L, result.getMemberEntityId());
        assertNotNull(result.getSentAt());
        assertFalse(result.isRead());

        // Verify in database
        Optional<MessageEntity> savedEntity = messageRepository.findById(result.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals("Test integration message", savedEntity.get().getContent());
    }

    @Test
    void testGetMessagesByBoxChatId_WithExistingData() {
        // Given - using test data from init.sql
        Long boxChatId = 1L;

        // When
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(boxChatId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Should contain messages from box chat 1
        boolean foundExpectedMessage = result.stream()
                .anyMatch(msg -> "Chào mọi người!".equals(msg.getContent()) ||
                        "Mình mới tham gia lớp yoga!".equals(msg.getContent()));
        assertTrue(foundExpectedMessage, "Should find expected messages from test data");
    }

    @Test
    void testGetMessagesByBoxChatId_EmptyResult() {
        // Given
        Long nonExistentBoxChatId = 999L;

        // When
        List<MessageResponse> result = messageService.getMessagesByBoxChatId(nonExistentBoxChatId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteMessage_Success() {
        // Given - Create a message first
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);
        messageDTO.setMemberEntityId(1L);
        messageDTO.setContent("Message to be deleted");

        MessageResponse createdMessage = messageService.sendMessage(messageDTO);
        Long messageId = createdMessage.getId();

        // Verify message exists
        assertTrue(messageRepository.existsById(messageId));

        // When
        messageService.deleteMessage(messageId);

        // Then
        assertFalse(messageRepository.existsById(messageId));
    }

    @Test
    void testDeleteMessage_NotFound_ThrowsException() {
        // Given
        Long nonExistentMessageId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(nonExistentMessageId));
    }

    @Test
    void testSendMessage_WithDifferentBoxChats() {
        // Given
        MessageDTO message1 = new MessageDTO();
        message1.setBoxChatEntityId(1L);
        message1.setMemberEntityId(1L);
        message1.setContent("Message for box chat 1");

        MessageDTO message2 = new MessageDTO();
        message2.setBoxChatEntityId(2L);
        message2.setMemberEntityId(2L);
        message2.setContent("Message for box chat 2");

        // When
        MessageResponse result1 = messageService.sendMessage(message1);
        MessageResponse result2 = messageService.sendMessage(message2);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getBoxChatEntityId(), result2.getBoxChatEntityId());

        // Verify messages are in correct box chats
        List<MessageResponse> boxChat1Messages = messageService.getMessagesByBoxChatId(1L);
        List<MessageResponse> boxChat2Messages = messageService.getMessagesByBoxChatId(2L);

        assertTrue(boxChat1Messages.stream().anyMatch(msg -> "Message for box chat 1".equals(msg.getContent())));
        assertTrue(boxChat2Messages.stream().anyMatch(msg -> "Message for box chat 2".equals(msg.getContent())));
    }

    @Test
    void testSendMessage_TimestampGeneration() {
        // Given
        long beforeTime = System.currentTimeMillis();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);
        messageDTO.setMemberEntityId(1L);
        messageDTO.setContent("Timestamp test message");

        // When
        MessageResponse result = messageService.sendMessage(messageDTO);
        long afterTime = System.currentTimeMillis();

        // Then
        assertNotNull(result.getSentAt());
        assertTrue(result.getSentAt().getTime() >= beforeTime);
        assertTrue(result.getSentAt().getTime() <= afterTime);
    }

    @Test
    void testGetMessagesByBoxChatId_OrderConsistency() {
        // Given - Create multiple messages for same box chat
        MessageDTO message1 = new MessageDTO();
        message1.setBoxChatEntityId(3L);
        message1.setMemberEntityId(1L);
        message1.setContent("First message");

        MessageDTO message2 = new MessageDTO();
        message2.setBoxChatEntityId(3L);
        message2.setMemberEntityId(2L);
        message2.setContent("Second message");

        MessageDTO message3 = new MessageDTO();
        message3.setBoxChatEntityId(3L);
        message3.setMemberEntityId(1L);
        message3.setContent("Third message");

        List<MessageResponse> messages = messageService.getMessagesByBoxChatId(3L);

        // Then
        assertNotNull(messages);
        assertTrue(messages.size() >= 3);

        // Verify all our messages are included
        assertTrue(messages.stream().anyMatch(msg -> "First message".equals(msg.getContent())));
        assertTrue(messages.stream().anyMatch(msg -> "Second message".equals(msg.getContent())));
        assertTrue(messages.stream().anyMatch(msg -> "Third message".equals(msg.getContent())));
    }

    @Test
    void testSendMessage_WithLongContent() {
        // Given
        String longContent = "This is a very long message content to test how the system handles larger text. "
                .repeat(10);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);
        messageDTO.setMemberEntityId(1L);
        messageDTO.setContent(longContent);

        // When
        MessageResponse result = messageService.sendMessage(messageDTO);

        // Then
        assertNotNull(result);
        assertEquals(longContent, result.getContent());

        // Verify in database
        Optional<MessageEntity> savedEntity = messageRepository.findById(result.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals(longContent, savedEntity.get().getContent());
    }

    @Test
    void testSendMessage_WithSpecialCharacters() {
        // Given
        String specialContent = "Xin chào! 🎉 Đây là tin nhắn có ký tự đặc biệt: @#$%^&*()";
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);
        messageDTO.setMemberEntityId(1L);
        messageDTO.setContent(specialContent);

        // When
        MessageResponse result = messageService.sendMessage(messageDTO);

        // Then
        assertNotNull(result);
        assertEquals(specialContent, result.getContent());

        // Verify in database
        Optional<MessageEntity> savedEntity = messageRepository.findById(result.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals(specialContent, savedEntity.get().getContent());
    }

    @Test
    void testDataConsistency_CreateAndDelete() {
        // Given
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);
        messageDTO.setMemberEntityId(1L);
        messageDTO.setContent("Consistency test message");

        // When - Create
        MessageResponse created = messageService.sendMessage(messageDTO);
        Long messageId = created.getId();

        // Verify creation
        assertTrue(messageRepository.existsById(messageId));
        List<MessageResponse> messagesAfterCreate = messageService.getMessagesByBoxChatId(1L);
        long countAfterCreate = messagesAfterCreate.stream()
                .filter(msg -> "Consistency test message".equals(msg.getContent()))
                .count();
        assertEquals(1, countAfterCreate);

        // When - Delete
        messageService.deleteMessage(messageId);

        // Then - Verify deletion
        assertFalse(messageRepository.existsById(messageId));
        List<MessageResponse> messagesAfterDelete = messageService.getMessagesByBoxChatId(1L);
        long countAfterDelete = messagesAfterDelete.stream()
                .filter(msg -> "Consistency test message".equals(msg.getContent()))
                .count();
        assertEquals(0, countAfterDelete);
    }

    @Test
    void testGetMessagesByBoxChatId_WithDifferentMembers() {
        // Given - Create messages from different members in same box chat
        MessageDTO message1 = new MessageDTO();
        message1.setBoxChatEntityId(4L);
        message1.setMemberEntityId(1L);
        message1.setContent("Message from member 1");

        MessageDTO message2 = new MessageDTO();
        message2.setBoxChatEntityId(4L);
        message2.setMemberEntityId(2L);
        message2.setContent("Message from member 2");

        // When
        messageService.sendMessage(message1);
        messageService.sendMessage(message2);

        List<MessageResponse> messages = messageService.getMessagesByBoxChatId(4L);

        // Then
        assertNotNull(messages);
        assertTrue(messages.size() >= 2);

        boolean hasMember1Message = messages.stream()
                .anyMatch(
                        msg -> msg.getMemberEntityId().equals(1L) && "Message from member 1".equals(msg.getContent()));
        boolean hasMember2Message = messages.stream()
                .anyMatch(
                        msg -> msg.getMemberEntityId().equals(2L) && "Message from member 2".equals(msg.getContent()));

        assertTrue(hasMember1Message);
        assertTrue(hasMember2Message);
    }
}
