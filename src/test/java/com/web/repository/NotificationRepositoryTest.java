package com.web.repository;

import com.web.entity.NotificationEntity;
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
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void findNotificationEntitiesByUserEntityId() {
        List<NotificationEntity> notificationEntities = notificationRepository.findNotificationEntitiesByUserEntityId(1L);
        Assertions.assertNotNull(notificationEntities);
        Assertions.assertEquals(1, notificationEntities.size(), "Expected 3 notifications for receiver with ID 1");
    }
}