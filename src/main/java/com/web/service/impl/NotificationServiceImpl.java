package com.web.service.impl;

import com.web.converter.NotificationConverter;
import com.web.entity.NotificationEntity;
import com.web.exception.sql.DataConflictException;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.repository.NotificationRepository;
import com.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationConverter notificationConverter;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(
            @Qualifier("notificationConverter") NotificationConverter notificationConverter,
            @Qualifier("notificationRepository") NotificationRepository notificationRepository) {
        this.notificationConverter = notificationConverter;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<NotificationResponse> getAllNotificationsByReceiverId(NotificationDTO notificationDTO) {
        return notificationRepository.findNotificationEntitiesByReceiverId(notificationDTO.getReceiverId()).stream().map(notificationConverter::toResponse).toList();
    }

    @Override
    public NotificationResponse sendNotification(NotificationDTO notificationDTO) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationDTO.getId());
        if (notificationEntityOptional.isPresent()) {
            throw new EntityAlreadyExistException(NotificationEntity.class);
        }
        NotificationEntity notificationEntity = notificationConverter.toEntity(notificationDTO);
        notificationEntityOptional = Optional.of(notificationRepository.save(notificationEntity));
        return notificationEntityOptional.map(notificationConverter::toResponse)
                .orElseThrow(() -> new DataConflictException(NotificationEntity.class));
    }

    @Override
    public NotificationResponse getNotificationById(NotificationDTO notificationDTO) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationDTO.getId());
        return notificationEntityOptional.map(notificationConverter::toResponse).orElseThrow(() -> new EntityNotFoundException(NotificationEntity.class));
    }

    @Override
    public void markAsRead(NotificationDTO notificationDTO) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationDTO.getId());
        NotificationEntity notificationEntity = notificationEntityOptional.orElseThrow(() -> new EntityNotFoundException(NotificationEntity.class));
        notificationEntity.setRead(true);
        notificationRepository.save(notificationEntity);
    }

    @Override
    public void deleteNotification(NotificationDTO notificationDTO) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationDTO.getId());
        notificationEntityOptional.orElseThrow(() -> new EntityNotFoundException(NotificationEntity.class));
        notificationRepository.deleteById(notificationDTO.getId());
    }
}
