package com.web.service;

import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.service.impl.NotificationServiceImpl;
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
class NotificationServiceIntegrationTest {
    @Autowired
    private NotificationServiceImpl notificationService;

    @Test
    void getAllNotificationsByReceiverId_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(1L);
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(dto);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void sendNotification_and_deleteNotification_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(1L);
        dto.setTitle("Test");
        dto.setContent("Nội dung test");
        NotificationResponse created = notificationService.sendNotification(dto);
        Assertions.assertNotNull(created.getId());
        // Xóa
        NotificationDTO delDto = new NotificationDTO();
        delDto.setId(created.getId());
        notificationService.deleteNotification(delDto);
        Assertions.assertThrows(Exception.class, () -> notificationService.deleteNotification(delDto));
    }

    @Test
    void getAllNotificationsByReceiverId_nullUserEntityId() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(null);
        Assertions.assertThrows(Exception.class, () -> notificationService.getAllNotificationsByReceiverId(dto));
    }

    @Test
    void sendNotification_duplicate() {
        // Gửi thông báo mới
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(1L);
        dto.setTitle("Test duplicate");
        dto.setContent("Nội dung duplicate");
        NotificationResponse created = notificationService.sendNotification(dto);
        // Thử gửi lại với cùng id (nếu service kiểm tra duplicate theo id)
        NotificationDTO duplicateDto = new NotificationDTO();
        duplicateDto.setId(created.getId());
        duplicateDto.setUserEntityId(1L);
        duplicateDto.setTitle("Test duplicate");
        duplicateDto.setContent("Nội dung duplicate");
        Assertions.assertThrows(Exception.class, () -> notificationService.sendNotification(duplicateDto));
        // Xóa thông báo test
        NotificationDTO delDto = new NotificationDTO();
        delDto.setId(created.getId());
        notificationService.deleteNotification(delDto);
    }

    @Test
    void deleteNotification_notFound() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(999999L);
        Assertions.assertThrows(Exception.class, () -> notificationService.deleteNotification(dto));
    }

    @Test
    void getAllNotificationsByReceiverId_notFound() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(999999L);
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(dto);
        Assertions.assertTrue(result.isEmpty());
    }
}