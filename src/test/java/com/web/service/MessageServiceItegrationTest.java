package com.web.service;

import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.service.impl.MessageServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MessageServiceItegrationTest {

    @Autowired
    private MessageServiceImpl messageService;

    @Test
    void getMessagesByBoxChatId() {
        // Create MessageDTO with box chat ID 1 (from initial data)
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L);

        // Get messages for that box chat
        List<MessageResponse> messages = messageService.getMessagesByBoxChatId(messageDTO);

        // Verify the results
        assertNotNull(messages);
        assertEquals(2, messages.size()); // Based on init.sql, box_chat_id 1 has 2 messages

        // Verify first message
        MessageResponse firstMessage = messages.stream()
                .filter(m -> m.getId() == 1L)
                .findFirst()
                .orElse(null);
        assertNotNull(firstMessage);
        assertEquals("Chào mọi người!", firstMessage.getContent());
        assertEquals(1L, firstMessage.getMemberEntityId());
        assertEquals(Timestamp.valueOf("2025-05-28 07:00:00.0"), firstMessage.getSentAt());
        assertFalse(firstMessage.isRead());
    }

    @Test
    void sendMessage() {
        // Create a new message
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBoxChatEntityId(1L); // Use existing box chat
        messageDTO.setMemberEntityId(2L); // Use existing sender/member
        messageDTO.setContent("Test message from integration test");

        // Send message
        MessageResponse sentMessage = messageService.sendMessage(messageDTO);

        // Verify sent message
        assertNotNull(sentMessage);
        assertNotNull(sentMessage.getId());
        assertEquals(messageDTO.getContent(), sentMessage.getContent());
        assertEquals(messageDTO.getBoxChatEntityId(), sentMessage.getBoxChatEntityId());
        assertEquals(messageDTO.getMemberEntityId(), sentMessage.getMemberEntityId());
        assertNotNull(sentMessage.getSentAt());
        assertFalse(sentMessage.isRead());

        // Verify the message was added to the box chat
        MessageDTO queryDTO = new MessageDTO();
        queryDTO.setBoxChatEntityId(1L);
        List<MessageResponse> updatedMessages = messageService.getMessagesByBoxChatId(queryDTO);

        assertTrue(updatedMessages.size() >= 3); // At least 3 messages now

        // Find our newly created message
        MessageResponse retrievedMessage = updatedMessages.stream()
                .filter(m -> m.getContent().equals("Test message from integration test"))
                .findFirst()
                .orElse(null);

        assertNotNull(retrievedMessage);
        assertEquals(sentMessage.getId(), retrievedMessage.getId());
    }

    @Test
    void deleteMessage() {
        // Create a new message first
        MessageDTO createDTO = new MessageDTO();
        createDTO.setBoxChatEntityId(1L);
        createDTO.setMemberEntityId(2L);
        createDTO.setContent("Message to be deleted");

        MessageResponse createdMessage = messageService.sendMessage(createDTO);
        assertNotNull(createdMessage);
        assertNotNull(createdMessage.getId());

        // Now delete the message
        MessageDTO deleteDTO = new MessageDTO();
        deleteDTO.setId(createdMessage.getId());

        messageService.deleteMessage(deleteDTO);

        // Verify the message is deleted - try to find it in the box chat
        MessageDTO queryDTO = new MessageDTO();
        queryDTO.setBoxChatEntityId(1L);
        List<MessageResponse> messages = messageService.getMessagesByBoxChatId(queryDTO);

        boolean messageFound = messages.stream()
                .anyMatch(m -> m.getId().equals(createdMessage.getId()));

        assertFalse(messageFound, "Deleted message should not be found");
    }
}