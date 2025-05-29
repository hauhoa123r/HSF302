package com.web.repository;

import com.web.entity.MessageEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Transactional
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Test
    void findByBoxChatEntityId() {
        List<MessageEntity> messageEntities = messageRepository.findByBoxChatEntityId(1L);
        Assertions.assertNotNull(messageEntities);
        Assertions.assertEquals(2, messageEntities.size(), "Expected 2 messages for receiver with ID 1");
    }
}