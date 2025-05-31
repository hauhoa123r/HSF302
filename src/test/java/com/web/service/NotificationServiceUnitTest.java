package com.web.service;

import com.web.entity.NotificationEntity;
import com.web.exception.ResourceNotFoundException;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.repository.NotificationRepository;
import com.web.service.impl.NotificationServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class NotificationServiceUnitTest {
    @Autowired
    private NotificationServiceImpl notificationService;

    @MockBean
    private NotificationRepository notificationRepositoryMock;

    // --- getAllNotificationsByReceiverId ---
    @Test
    void getAllNotificationsByReceiverId_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(1L);
        NotificationEntity entity = new NotificationEntity();
        entity.setId(1L);
        Mockito.when(notificationRepositoryMock.findNotificationEntitiesByUserEntityId(1L)).thenReturn(List.of(entity));
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(dto);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
    }

    @Test
    void getAllNotificationsByReceiverId_nullUserEntityId() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(null);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> notificationService.getAllNotificationsByReceiverId(dto));
    }

    @Test
    void getAllNotificationsByReceiverId_notFound() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(999L);
        Mockito.when(notificationRepositoryMock.findNotificationEntitiesByUserEntityId(999L)).thenReturn(List.of());
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(dto);
        Assertions.assertTrue(result.isEmpty());
    }

    // --- sendNotification ---
    @Test
    void sendNotification_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        dto.setUserEntityId(2L);
        dto.setTitle("Test");
        dto.setContent("Test content");
        NotificationEntity entity = new NotificationEntity();
        entity.setId(1L);
        Mockito.when(notificationRepositoryMock.existsById(1L)).thenReturn(false);
        Mockito.when(notificationRepositoryMock.save(Mockito.any(NotificationEntity.class))).thenReturn(entity);
        NotificationResponse res = notificationService.sendNotification(dto);
        Assertions.assertEquals(1L, res.getId());
    }

    @Test
    void sendNotification_duplicate() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(2L);
        dto.setUserEntityId(2L);
        dto.setTitle("Test");
        dto.setContent("Test content");
        Mockito.when(notificationRepositoryMock.existsById(2L)).thenReturn(true);
        Assertions.assertThrows(EntityAlreadyExistException.class, () -> notificationService.sendNotification(dto));
    }

    @Test
    void sendNotification_nullContent() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(2L);
        dto.setTitle("Test");
        dto.setContent(null);
        Assertions.assertThrows(Exception.class, () -> notificationService.sendNotification(dto));
    }

    @Test
    void sendNotification_nullTitle() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(2L);
        dto.setTitle(null);
        dto.setContent("Test");
        Assertions.assertThrows(Exception.class, () -> notificationService.sendNotification(dto));
    }

    @Test
    void sendNotification_nullUserEntityId() {
        NotificationDTO dto = new NotificationDTO();
        dto.setUserEntityId(null);
        dto.setTitle("Test");
        dto.setContent("Test");
        Assertions.assertThrows(Exception.class, () -> notificationService.sendNotification(dto));
    }

    // --- markAsRead ---
    @Test
    void markAsRead_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        NotificationEntity entity = new NotificationEntity();
        entity.setId(1L);
        entity.setRead(false);
        Mockito.when(notificationRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(notificationRepositoryMock.findById(1L)).thenReturn(Optional.of(entity));
        notificationService.markAsRead(dto);
        Assertions.assertTrue(entity.isRead());
    }

    @Test
    void markAsRead_nullId() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> notificationService.markAsRead(dto));
    }

    @Test
    void markAsRead_notFound() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(999L);
        Mockito.when(notificationRepositoryMock.existsById(999L)).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> notificationService.markAsRead(dto));
    }

    // --- deleteNotification ---
    @Test
    void deleteNotification_success() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(1L);
        NotificationEntity entity = new NotificationEntity();
        entity.setId(1L);
        Mockito.when(notificationRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(notificationRepositoryMock.findById(1L)).thenReturn(Optional.of(entity));
        notificationService.deleteNotification(dto);
        Mockito.verify(notificationRepositoryMock).deleteById(1L);
    }

    @Test
    void deleteNotification_nullId() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> notificationService.deleteNotification(dto));
    }

    @Test
    void deleteNotification_notFound() {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(999L);
        Mockito.when(notificationRepositoryMock.existsById(999L)).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> notificationService.deleteNotification(dto));
    }
}