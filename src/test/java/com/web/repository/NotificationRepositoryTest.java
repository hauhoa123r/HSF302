package com.web.repository;

import com.web.entity.NotificationEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void findNotificationEntitiesByReceiverId() {
        List<NotificationEntity> notificationEntities = notificationRepository.findNotificationEntitiesByReceiverId(1L);
        Assertions.assertNotNull(notificationEntities);
        Assertions.assertEquals(1, notificationEntities.size(), "Expected 3 notifications for receiver with ID 1");
    }
}