package com.web.service;

import com.web.converter.NotificationConverter;
import com.web.entity.NotificationEntity;
import com.web.entity.UserEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.repository.NotificationRepository;
import com.web.service.impl.NotificationServiceImpl;
import com.web.utils.CheckFieldObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class NotificationServiceUnitTest {

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private NotificationConverter notificationConverter;

    @MockBean
    private CheckFieldObject checkFieldObject;

    private NotificationService notificationService;

    private NotificationDTO notificationDTO;
    private NotificationEntity notificationEntity;
    private NotificationResponse notificationResponse;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationServiceImpl();
        ((NotificationServiceImpl) notificationService).setNotificationRepository(notificationRepository);
        ((NotificationServiceImpl) notificationService).setNotificationConverter(notificationConverter);
        ((NotificationServiceImpl) notificationService).setCheckFieldObject(checkFieldObject);

        // Setup test data
        userEntity = new UserEntity();
        userEntity.setId(1L);

        notificationDTO = new NotificationDTO();
        notificationDTO.setUserEntityId(1L);
        notificationDTO.setTitle("Test notification");
        notificationDTO.setContent("Test notification content");

        notificationEntity = new NotificationEntity();
        notificationEntity.setId(1L);
        notificationEntity.setUserEntity(userEntity);
        notificationEntity.setTitle("Test notification");
        notificationEntity.setContent("Test notification content");
        notificationEntity.setRead(false);
        notificationEntity.setSentAt(new Timestamp(System.currentTimeMillis()));

        notificationResponse = new NotificationResponse();
        notificationResponse.setId(1L);
        notificationResponse.setUserEntityId(1L);
        notificationResponse.setTitle("Test notification");
        notificationResponse.setContent("Test notification content");
        notificationResponse.setRead(false);
        notificationResponse.setSentAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testGetAllNotificationsByReceiverId_Success() {
        // Given
        Long receiverId = 1L;
        List<NotificationEntity> notificationEntities = Arrays.asList(notificationEntity);
        List<NotificationResponse> expectedResponses = Arrays.asList(notificationResponse);

        when(notificationRepository.findAllByUserEntityId(receiverId)).thenReturn(notificationEntities);
        when(notificationConverter.toResponse(notificationEntity)).thenReturn(notificationResponse);

        // When
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(receiverId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedResponses.get(0).getId(), result.get(0).getId());
        assertEquals(expectedResponses.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(expectedResponses.get(0).getContent(), result.get(0).getContent());

        verify(notificationRepository).findAllByUserEntityId(receiverId);
        verify(notificationConverter).toResponse(notificationEntity);
    }

    @Test
    void testGetAllNotificationsByReceiverId_EmptyList() {
        // Given
        Long receiverId = 1L;
        when(notificationRepository.findAllByUserEntityId(receiverId)).thenReturn(Collections.emptyList());

        // When
        List<NotificationResponse> result = notificationService.getAllNotificationsByReceiverId(receiverId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(notificationRepository).findAllByUserEntityId(receiverId);
        verify(notificationConverter, never()).toResponse(any());
    }

    @Test
    void testSendNotification_Success() {
        // Given
        when(notificationRepository.existsById(anyLong())).thenReturn(false);
        when(notificationConverter.toEntity(notificationDTO)).thenReturn(notificationEntity);
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notificationEntity);
        when(notificationConverter.toResponse(notificationEntity)).thenReturn(notificationResponse);

        // When
        NotificationResponse result = notificationService.sendNotification(notificationDTO);

        // Then
        assertNotNull(result);
        assertEquals(notificationResponse.getId(), result.getId());
        assertEquals(notificationResponse.getTitle(), result.getTitle());
        assertEquals(notificationResponse.getContent(), result.getContent());
        assertFalse(result.isRead());

        verify(checkFieldObject).check(NotificationDTO.class, notificationDTO);
        verify(notificationConverter).toEntity(notificationDTO);
        verify(notificationRepository).save(any(NotificationEntity.class));
        verify(notificationConverter).toResponse(notificationEntity);

        // Verify that isRead was set to false and sentAt was set
        ArgumentCaptor<NotificationEntity> entityCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(entityCaptor.capture());
        NotificationEntity savedEntity = entityCaptor.getValue();
        assertFalse(savedEntity.isRead());
        assertNotNull(savedEntity.getSentAt());
    }

    @Test
    void testSendNotification_WithExistingId_ThrowsEntityAlreadyExistException() {
        // Given
        notificationDTO.setId(1L);
        when(notificationRepository.existsById(1L)).thenReturn(true);

        // When & Then
        assertThrows(EntityAlreadyExistException.class, () -> notificationService.sendNotification(notificationDTO));

        verify(checkFieldObject).check(NotificationDTO.class, notificationDTO);
        verify(notificationRepository).existsById(1L);
        verify(notificationConverter, never()).toEntity(any());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testMarkAsRead_Success() {
        // Given
        Long notificationId = 1L;
        notificationEntity.setRead(false);

        when(notificationRepository.existsById(notificationId)).thenReturn(true);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationEntity));
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notificationEntity);

        // When
        notificationService.markAsRead(notificationId);

        // Then
        verify(notificationRepository).existsById(notificationId);
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(any(NotificationEntity.class));

        // Verify that isRead was set to true
        ArgumentCaptor<NotificationEntity> entityCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(entityCaptor.capture());
        assertTrue(entityCaptor.getValue().isRead());
    }

    @Test
    void testMarkAsRead_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long notificationId = 1L;
        when(notificationRepository.existsById(notificationId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> notificationService.markAsRead(notificationId));

        verify(notificationRepository).existsById(notificationId);
        verify(notificationRepository, never()).findById(any());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testMarkAsUnread_Success() {
        // Given
        Long notificationId = 1L;
        notificationEntity.setRead(true);

        when(notificationRepository.existsById(notificationId)).thenReturn(true);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notificationEntity));
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notificationEntity);

        // When
        notificationService.markAsUnread(notificationId);

        // Then
        verify(notificationRepository).existsById(notificationId);
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(any(NotificationEntity.class));

        // Verify that isRead was set to false
        ArgumentCaptor<NotificationEntity> entityCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(entityCaptor.capture());
        assertFalse(entityCaptor.getValue().isRead());
    }

    @Test
    void testMarkAsUnread_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long notificationId = 1L;
        when(notificationRepository.existsById(notificationId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> notificationService.markAsUnread(notificationId));

        verify(notificationRepository).existsById(notificationId);
        verify(notificationRepository, never()).findById(any());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testMarkAllAsRead_Success() {
        // Given
        Long receiverId = 1L;
        NotificationEntity notification1 = new NotificationEntity();
        notification1.setId(1L);
        notification1.setRead(false);

        NotificationEntity notification2 = new NotificationEntity();
        notification2.setId(2L);
        notification2.setRead(false);

        List<NotificationEntity> notifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findAllByUserEntityId(receiverId)).thenReturn(notifications);
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notification1, notification2);

        // When
        notificationService.markAllAsRead(receiverId);

        // Then
        verify(notificationRepository).findAllByUserEntityId(receiverId);
        verify(notificationRepository, times(2)).save(any(NotificationEntity.class));

        // Verify that all notifications were marked as read
        ArgumentCaptor<NotificationEntity> entityCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository, times(2)).save(entityCaptor.capture());
        List<NotificationEntity> savedEntities = entityCaptor.getAllValues();
        assertTrue(savedEntities.get(0).isRead());
        assertTrue(savedEntities.get(1).isRead());
    }

    @Test
    void testMarkAllAsRead_EmptyList() {
        // Given
        Long receiverId = 1L;
        when(notificationRepository.findAllByUserEntityId(receiverId)).thenReturn(Collections.emptyList());

        // When
        notificationService.markAllAsRead(receiverId);

        // Then
        verify(notificationRepository).findAllByUserEntityId(receiverId);
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testDeleteNotification_Success() {
        // Given
        Long notificationId = 1L;
        when(notificationRepository.existsById(notificationId)).thenReturn(true);

        // When
        notificationService.deleteNotification(notificationId);

        // Then
        verify(notificationRepository).existsById(notificationId);
        verify(notificationRepository).deleteById(notificationId);
    }

    @Test
    void testDeleteNotification_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long notificationId = 1L;
        when(notificationRepository.existsById(notificationId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> notificationService.deleteNotification(notificationId));

        verify(notificationRepository).existsById(notificationId);
        verify(notificationRepository, never()).deleteById(any());
    }

    @Test
    void testSendNotification_TimestampSetCorrectly() {
        // Given
        long beforeTime = System.currentTimeMillis();
        when(notificationConverter.toEntity(notificationDTO)).thenReturn(notificationEntity);
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notificationEntity);
        when(notificationConverter.toResponse(notificationEntity)).thenReturn(notificationResponse);

        // When
        notificationService.sendNotification(notificationDTO);
        long afterTime = System.currentTimeMillis();

        // Then
        ArgumentCaptor<NotificationEntity> entityCaptor = ArgumentCaptor.forClass(NotificationEntity.class);
        verify(notificationRepository).save(entityCaptor.capture());

        NotificationEntity savedEntity = entityCaptor.getValue();
        assertNotNull(savedEntity.getSentAt());
        assertTrue(savedEntity.getSentAt().getTime() >= beforeTime);
        assertTrue(savedEntity.getSentAt().getTime() <= afterTime);
        assertFalse(savedEntity.isRead());
    }

    @Test
    void testSendNotification_WithNullId_Success() {
        // Given
        notificationDTO.setId(null);
        when(notificationConverter.toEntity(notificationDTO)).thenReturn(notificationEntity);
        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(notificationEntity);
        when(notificationConverter.toResponse(notificationEntity)).thenReturn(notificationResponse);

        // When
        NotificationResponse result = notificationService.sendNotification(notificationDTO);

        // Then
        assertNotNull(result);
        assertEquals(notificationResponse.getId(), result.getId());

        verify(checkFieldObject).check(NotificationDTO.class, notificationDTO);
        verify(notificationRepository, never()).existsById(any());
        verify(notificationConverter).toEntity(notificationDTO);
        verify(notificationRepository).save(any(NotificationEntity.class));
        verify(notificationConverter).toResponse(notificationEntity);
    }
}
