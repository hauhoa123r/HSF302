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
    void markAsRead_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(1L);
        List<NotificationResponse> list = notificationService.getAllNotificationsByReceiverId(dto);
        if (!list.isEmpty()) {
            NotificationDTO markDto = new NotificationDTO();
            markDto.setId(list.get(0).getId());
            notificationService.markAsRead(markDto);
            List<NotificationResponse> updated = notificationService.getAllNotificationsByReceiverId(dto);
            NotificationResponse res = updated.stream().filter(n -> n.getId().equals(markDto.getId())).findFirst().orElse(null);
            Assertions.assertNotNull(res);
            Assertions.assertTrue(res.isRead());
        }
    }

    @Test
    void markAsRead_notFound() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(999999L);
        Assertions.assertThrows(Exception.class, () -> notificationService.markAsRead(dto));
    }

    @Test
    void markAsUnread_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(1L);
        List<NotificationResponse> list = notificationService.getAllNotificationsByReceiverId(dto);
        if (!list.isEmpty()) {
            NotificationDTO markDto = new NotificationDTO();
            markDto.setId(list.get(0).getId());
            notificationService.markAsUnread(markDto);
            List<NotificationResponse> updated = notificationService.getAllNotificationsByReceiverId(dto);
            NotificationResponse res = updated.stream().filter(n -> n.getId().equals(markDto.getId())).findFirst().orElse(null);
            Assertions.assertNotNull(res);
            Assertions.assertFalse(res.isRead());
        }
    }

    @Test
    void markAsUnread_notFound() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(999999L);
        Assertions.assertThrows(Exception.class, () -> notificationService.markAsUnread(dto));
    }

    @Test
    void markAllAsRead_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(1L);
        notificationService.markAllAsRead(dto);
        List<NotificationResponse> list = notificationService.getAllNotificationsByReceiverId(dto);
        for (NotificationResponse res : list) {
            Assertions.assertTrue(res.isRead());
        }
    }

    @Test
    void markAllAsRead_nullUserEntityId() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(null);
        Assertions.assertThrows(Exception.class, () -> notificationService.markAllAsRead(dto));
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