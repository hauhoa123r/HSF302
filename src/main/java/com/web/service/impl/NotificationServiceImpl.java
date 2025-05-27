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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationConverter notificationConverter;

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
        notificationEntityOptional = Optional.ofNullable(notificationRepository.save(notificationEntity));
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
        notificationEntityOptional = Optional.ofNullable(notificationRepository.save(notificationEntity));
        notificationEntityOptional.orElseThrow(() -> new DataConflictException(NotificationEntity.class));
    }

    @Override
    public void deleteNotification(NotificationDTO notificationDTO) {
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(notificationDTO.getId());
        notificationEntityOptional.orElseThrow(() -> new EntityNotFoundException(NotificationEntity.class));
        notificationRepository.deleteById(notificationDTO.getId());
    }
}
