package com.web.service;

import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.service.impl.NotificationServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificationServiceIntegrationTest {
    @Autowired
    private NotificationServiceImpl notificationService;

    @Test
    void getAllNotificationsByReceiverId() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);

        List<NotificationResponse> notifications = notificationService.getAllNotificationsByReceiverId(notificationDTO);
        assertNotNull(notifications);
        assertEquals(1, notifications.size());

        NotificationResponse notificationResponse = notifications.get(0);
        assertEquals(1L, notificationResponse.getId());
        assertEquals("Chào mừng", notificationResponse.getTitle());
        assertEquals("Chào mừng bạn đến với phòng gym!", notificationResponse.getContent());
        assertEquals(Timestamp.valueOf("2025-05-01 09:00:00.0"), notificationResponse.getSentAt());
        assertFalse(notificationResponse.isRead());
        assertEquals(1L, notificationResponse.getUserEntityId());
    }

    @Test
    void sendNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(2L);
        notificationDTO.setTitle("Test Notification");
        notificationDTO.setContent("This is a test notification.");

        NotificationResponse sentNotification = notificationService.sendNotification(notificationDTO);

        assertNotNull(sentNotification);
        assertEquals(notificationDTO.getUserEntityId(), sentNotification.getUserEntityId());
        assertEquals(notificationDTO.getTitle(), sentNotification.getTitle());
        assertEquals(notificationDTO.getContent(), sentNotification.getContent());
        assertFalse(sentNotification.isRead());
    }

    @Test
    void markAsRead() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        notificationService.markAsRead(notificationDTO);

        NotificationDTO getNotification = new NotificationDTO();
        getNotification.setUserEntityId(1L);
        List<NotificationResponse> notifications = notificationService.getAllNotificationsByReceiverId(getNotification);
        assertTrue(notifications.get(0).isRead());
    }

    @Test
    void deleteNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        notificationService.deleteNotification(notificationDTO);

        NotificationDTO getNotification = new NotificationDTO();
        getNotification.setUserEntityId(1L);
        List<NotificationResponse> notifications = notificationService.getAllNotificationsByReceiverId(getNotification);
        assertEquals(0, notifications.size());
    }
}