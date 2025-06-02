package com.web.service;

import com.web.entity.NotificationEntity;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.repository.NotificationRepository;
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
class NotificationServiceIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void testSendNotification_Success() {
        // Given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Integration Test Notification");
        notificationDTO.setContent("This is a test notification for integration testing");

        // When
        NotificationResponse result = notificationService.sendNotification(notificationDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Integration Test Notification", result.getTitle());
        assertEquals("This is a test notification for integration testing", result.getContent());
        assertEquals(1L, result.getUserEntityId());
        assertNotNull(result.getSentAt());
        assertFalse(result.isRead());

        // Verify in database
        Optional<NotificationEntity> savedEntity = notificationRepository.findById(result.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals("Integration Test Notification", savedEntity.get().getTitle());
        assertFalse(savedEntity.get().isRead());
    }

    @Test
    void testGetAllNotificationsByReceiverId_WithExistingData() {
        // Given - using test data from init.sql
        Long receiverId = 1L;

        // When
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(receiverId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Should contain notification from test data
        boolean foundExpectedNotification = result.stream()
                .anyMatch(notification -> "Chào mừng".equals(notification.getTitle()) &&
                        "Chào mừng bạn đến với phòng gym!".equals(notification.getContent()));
        assertTrue(foundExpectedNotification, "Should find expected notification from test data");
    }

    @Test
    void testGetAllNotificationsByReceiverId_EmptyResult() {
        // Given
        Long nonExistentReceiverId = 999L;

        // When
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(nonExistentReceiverId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMarkAsRead_Success() {
        // Given - Create a notification first
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Mark as Read Test");
        notificationDTO.setContent("Test notification for marking as read");

        NotificationResponse createdNotification = notificationService.sendNotification(notificationDTO);
        Long notificationId = createdNotification.getId();

        // Verify notification is initially unread
        Optional<NotificationEntity> beforeUpdate = notificationRepository.findById(notificationId);
        assertTrue(beforeUpdate.isPresent());
        assertFalse(beforeUpdate.get().isRead());

        // When
        notificationService.markAsRead(notificationId);

        // Then
        Optional<NotificationEntity> afterUpdate = notificationRepository.findById(notificationId);
        assertTrue(afterUpdate.isPresent());
        assertTrue(afterUpdate.get().isRead());
    }

    @Test
    void testMarkAsRead_NotFound_ThrowsException() {
        // Given
        Long nonExistentNotificationId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> notificationService.markAsRead(nonExistentNotificationId));
    }

    @Test
    void testMarkAsUnread_Success() {
        // Given - Create a notification and mark it as read first
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Mark as Unread Test");
        notificationDTO.setContent("Test notification for marking as unread");

        NotificationResponse createdNotification = notificationService.sendNotification(notificationDTO);
        Long notificationId = createdNotification.getId();

        // Mark as read first
        notificationService.markAsRead(notificationId);
        Optional<NotificationEntity> afterRead = notificationRepository.findById(notificationId);
        assertTrue(afterRead.isPresent());
        assertTrue(afterRead.get().isRead());

        // When
        notificationService.markAsUnread(notificationId);

        // Then
        Optional<NotificationEntity> afterUnread = notificationRepository.findById(notificationId);
        assertTrue(afterUnread.isPresent());
        assertFalse(afterUnread.get().isRead());
    }

    @Test
    void testMarkAsUnread_NotFound_ThrowsException() {
        // Given
        Long nonExistentNotificationId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> notificationService.markAsUnread(nonExistentNotificationId));
    }

    @Test
    void testMarkAllAsRead_Success() {
        // Given - Create multiple notifications for same user
        Long userId = 2L;

        NotificationDTO notification1 = new NotificationDTO();
        notification1.setUserEntityId(userId);
        notification1.setTitle("Notification 1");
        notification1.setContent("First notification");

        NotificationDTO notification2 = new NotificationDTO();
        notification2.setUserEntityId(userId);
        notification2.setTitle("Notification 2");
        notification2.setContent("Second notification");

        NotificationResponse result1 = notificationService.sendNotification(notification1);
        NotificationResponse result2 = notificationService.sendNotification(notification2);

        // Verify both are initially unread
        assertFalse(result1.isRead());
        assertFalse(result2.isRead());

        // When
        notificationService.markAllAsRead(userId);

        // Then
        Optional<NotificationEntity> entity1 = notificationRepository.findById(result1.getId());
        Optional<NotificationEntity> entity2 = notificationRepository.findById(result2.getId());

        assertTrue(entity1.isPresent());
        assertTrue(entity2.isPresent());
        assertTrue(entity1.get().isRead());
        assertTrue(entity2.get().isRead());
    }

    @Test
    void testDeleteNotification_Success() {
        // Given - Create a notification first
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Delete Test");
        notificationDTO.setContent("Notification to be deleted");

        NotificationResponse createdNotification = notificationService.sendNotification(notificationDTO);
        Long notificationId = createdNotification.getId();

        // Verify notification exists
        assertTrue(notificationRepository.existsById(notificationId));

        // When
        notificationService.deleteNotification(notificationId);

        // Then
        assertFalse(notificationRepository.existsById(notificationId));
    }

    @Test
    void testDeleteNotification_NotFound_ThrowsException() {
        // Given
        Long nonExistentNotificationId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> notificationService.deleteNotification(nonExistentNotificationId));
    }

    @Test
    void testSendNotification_WithVietnameseContent() {
        // Given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Thông báo quan trọng");
        notificationDTO
                .setContent("Xin chào! Đây là thông báo tiếng Việt có dấu. Bạn có lịch tập thể dục vào ngày mai.");

        // When
        NotificationResponse result = notificationService.sendNotification(notificationDTO);

        // Then
        assertNotNull(result);
        assertEquals("Thông báo quan trọng", result.getTitle());
        assertEquals("Xin chào! Đây là thông báo tiếng Việt có dấu. Bạn có lịch tập thể dục vào ngày mai.",
                result.getContent());

        // Verify in database
        Optional<NotificationEntity> savedEntity = notificationRepository.findById(result.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals("Thông báo quan trọng", savedEntity.get().getTitle());
        assertEquals("Xin chào! Đây là thông báo tiếng Việt có dấu. Bạn có lịch tập thể dục vào ngày mai.",
                savedEntity.get().getContent());
    }

    @Test
    void testSendNotification_WithLongContent() {
        // Given
        String longContent = "Đây là một thông báo rất dài để kiểm tra khả năng xử lý nội dung lớn của hệ thống. "
                .repeat(20);
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Thông báo dài");
        notificationDTO.setContent(longContent);

        // When
        NotificationResponse result = notificationService.sendNotification(notificationDTO);

        // Then
        assertNotNull(result);
        assertEquals(longContent, result.getContent());

        // Verify in database
        Optional<NotificationEntity> savedEntity = notificationRepository.findById(result.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals(longContent, savedEntity.get().getContent());
    }

    @Test
    void testSendNotification_TimestampGeneration() {
        // Given
        long beforeTime = System.currentTimeMillis();
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Timestamp Test");
        notificationDTO.setContent("Testing timestamp generation");

        // When
        NotificationResponse result = notificationService.sendNotification(notificationDTO);
        long afterTime = System.currentTimeMillis();

        // Then
        assertNotNull(result.getSentAt());
        assertTrue(result.getSentAt().getTime() >= beforeTime);
        assertTrue(result.getSentAt().getTime() <= afterTime);
    }

    @Test
    void testGetAllNotificationsByReceiverId_MultipleUsers() {
        // Given - Create notifications for different users
        NotificationDTO notification1 = new NotificationDTO();
        notification1.setUserEntityId(3L);
        notification1.setTitle("User 3 Notification");
        notification1.setContent("Notification for user 3");

        NotificationDTO notification2 = new NotificationDTO();
        notification2.setUserEntityId(4L);
        notification2.setTitle("User 4 Notification");
        notification2.setContent("Notification for user 4");

        notificationService.sendNotification(notification1);
        notificationService.sendNotification(notification2);

        // When
        List<NotificationResponse> user3Notifications = notificationService.getAllNotificationsByReceiverId(3L);
        List<NotificationResponse> user4Notifications = notificationService.getAllNotificationsByReceiverId(4L);

        // Then
        assertNotNull(user3Notifications);
        assertNotNull(user4Notifications);

        boolean hasUser3Notification = user3Notifications.stream()
                .anyMatch(notification -> "User 3 Notification".equals(notification.getTitle()));
        boolean hasUser4Notification = user4Notifications.stream()
                .anyMatch(notification -> "User 4 Notification".equals(notification.getTitle()));

        assertTrue(hasUser3Notification);
        assertTrue(hasUser4Notification);

        // Verify user 3 notifications don't contain user 4 notifications and vice versa
        boolean user3HasUser4Notification = user3Notifications.stream()
                .anyMatch(notification -> "User 4 Notification".equals(notification.getTitle()));
        boolean user4HasUser3Notification = user4Notifications.stream()
                .anyMatch(notification -> "User 3 Notification".equals(notification.getTitle()));

        assertFalse(user3HasUser4Notification);
        assertFalse(user4HasUser3Notification);
    }

    @Test
    void testDataConsistency_CreateMarkReadDelete() {
        // Given
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Consistency Test");
        notificationDTO.setContent("Testing data consistency");

        // When - Create
        NotificationResponse created = notificationService.sendNotification(notificationDTO);
        Long notificationId = created.getId();

        // Verify creation
        assertTrue(notificationRepository.existsById(notificationId));
        List<NotificationResponse> notificationsAfterCreate = notificationService.getAllNotificationsByReceiverId(1L);
        long countAfterCreate = notificationsAfterCreate.stream()
                .filter(notification -> "Consistency Test".equals(notification.getTitle()))
                .count();
        assertTrue(countAfterCreate >= 1);

        // When - Mark as read
        notificationService.markAsRead(notificationId);

        // Verify mark as read
        Optional<NotificationEntity> afterRead = notificationRepository.findById(notificationId);
        assertTrue(afterRead.isPresent());
        assertTrue(afterRead.get().isRead());

        // When - Delete
        notificationService.deleteNotification(notificationId);

        // Then - Verify deletion
        assertFalse(notificationRepository.existsById(notificationId));
        List<NotificationResponse> notificationsAfterDelete = notificationService.getAllNotificationsByReceiverId(1L);
        long countAfterDelete = notificationsAfterDelete.stream()
                .filter(notification -> "Consistency Test".equals(notification.getTitle()))
                .count();
        assertEquals(0, countAfterDelete);
    }

    @Test
    void testMarkAllAsRead_WithMixedReadStatus() {
        // Given - Create notifications with mixed read status
        Long userId = 5L;

        NotificationDTO notification1 = new NotificationDTO();
        notification1.setUserEntityId(userId);
        notification1.setTitle("Unread Notification");
        notification1.setContent("This will be unread");

        NotificationDTO notification2 = new NotificationDTO();
        notification2.setUserEntityId(userId);
        notification2.setTitle("Read Notification");
        notification2.setContent("This will be read");

        NotificationResponse result1 = notificationService.sendNotification(notification1);
        NotificationResponse result2 = notificationService.sendNotification(notification2);

        // Mark one as read manually
        notificationService.markAsRead(result2.getId());

        // Verify mixed status
        Optional<NotificationEntity> entity1Before = notificationRepository.findById(result1.getId());
        Optional<NotificationEntity> entity2Before = notificationRepository.findById(result2.getId());
        assertTrue(entity1Before.isPresent());
        assertTrue(entity2Before.isPresent());
        assertFalse(entity1Before.get().isRead());
        assertTrue(entity2Before.get().isRead());

        // When
        notificationService.markAllAsRead(userId);

        // Then
        Optional<NotificationEntity> entity1After = notificationRepository.findById(result1.getId());
        Optional<NotificationEntity> entity2After = notificationRepository.findById(result2.getId());
        assertTrue(entity1After.isPresent());
        assertTrue(entity2After.isPresent());
        assertTrue(entity1After.get().isRead());
        assertTrue(entity2After.get().isRead());
    }
}
