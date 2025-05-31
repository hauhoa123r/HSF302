package com.web.service;

import com.web.entity.NotificationEntity;
import com.web.exception.sql.EntityAlreadyExistException;
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

    @Test
    void getAllNotificationsByReceiverId() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(1L);

        Mockito.when(notificationRepositoryMock.findNotificationEntitiesByUserEntityId((1L))).thenReturn(List.of(notificationEntity));
        List<NotificationResponse> notifications = notificationService.getAllNotificationsByReceiverId(notificationDTO);

        Mockito.verify(notificationRepositoryMock, Mockito.times(1)).findNotificationEntitiesByUserEntityId((1L));

        Assertions.assertEquals(1, notifications.size());
        Assertions.assertEquals(1, notifications.get(0).getId());
    }

    @Test
    void sendNotification() {
        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.setId(1L);

        NotificationDTO notificationDTO2 = new NotificationDTO();
        notificationDTO2.setId(2L);


        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(1L);

        Mockito.when(notificationRepositoryMock.existsById(1L)).thenReturn(false);
        Mockito.when(notificationRepositoryMock.save(Mockito.any(NotificationEntity.class))).thenReturn(notificationEntity);

        NotificationResponse notificationResponse = notificationService.sendNotification(notificationDTO1);
        Mockito.verify(notificationRepositoryMock, Mockito.times(1)).existsById(1L);
        Assertions.assertEquals(1L, notificationResponse.getId());

        Mockito.when(notificationRepositoryMock.existsById(2L)).thenReturn(true);
        Assertions.assertThrows(EntityAlreadyExistException.class, () -> notificationService.sendNotification(notificationDTO2));
        Mockito.verify(notificationRepositoryMock, Mockito.times(1)).existsById(2L);
    }

    @Test
    void markAsRead() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(1L);
        notificationEntity.setRead(false);

        Mockito.when(notificationRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(notificationRepositoryMock.findById(1L)).thenReturn(Optional.of(notificationEntity));
        notificationService.markAsRead(notificationDTO);

        Mockito.verify(notificationRepositoryMock, Mockito.times(1)).findById(1L);
        Mockito.verify(notificationRepositoryMock, Mockito.times(1)).save(notificationEntity);
        Assertions.assertTrue(notificationEntity.isRead());
    }

    @Test
    void deleteNotification() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(1L);

        Mockito.when(notificationRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(notificationRepositoryMock.findById(1L)).thenReturn(Optional.of(notificationEntity));
        notificationService.deleteNotification(notificationDTO);

        Mockito.verify(notificationRepositoryMock, Mockito.times(1)).findById(1L);
        Mockito.verify(notificationRepositoryMock, Mockito.times(1)).deleteById(1L);
    }
}