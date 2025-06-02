package com.web.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.converter.NotificationConverter;
import com.web.entity.NotificationEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.repository.NotificationRepository;
import com.web.service.NotificationService;
import com.web.utils.CheckFieldObject;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private NotificationConverter notificationConverter;
    private NotificationRepository notificationRepository;
    private CheckFieldObject checkFieldObject;

    @Autowired
    public void setNotificationConverter(NotificationConverter notificationConverter) {
        this.notificationConverter = notificationConverter;
    }

    @Autowired
    public void setNotificationRepository(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setCheckFieldObject(CheckFieldObject checkFieldObject) {
        this.checkFieldObject = checkFieldObject;
    }

    @Override
    public List<NotificationResponse> getAllNotificationsByReceiverId(Long receiverId) {
        return notificationRepository.findAllByUserEntityId(receiverId).stream().map(notificationConverter::toResponse).toList();
    }


    @Override
    public NotificationResponse sendNotification(NotificationDTO notificationDTO) {
        checkFieldObject.check(NotificationDTO.class, notificationDTO);

        if (notificationDTO.getId() != null && notificationRepository.existsById(notificationDTO.getId())) {
            throw new EntityAlreadyExistException(NotificationEntity.class);
        }

        NotificationEntity notificationEntity = notificationConverter.toEntity(notificationDTO);
        notificationEntity.setRead(false);
        notificationEntity.setSentAt(new Timestamp(System.currentTimeMillis()));
        Optional<NotificationEntity> notificationEntityOptional = Optional.of(notificationRepository.save(notificationEntity));
        return notificationConverter.toResponse(notificationEntityOptional.get());
    }

    @Override
    public void markAsRead(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new EntityNotFoundException(NotificationEntity.class);
        }
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(id);
        NotificationEntity notificationEntity = notificationEntityOptional.orElseThrow(() -> new EntityNotFoundException(NotificationEntity.class));
        notificationEntity.setRead(true);
        notificationRepository.save(notificationEntity);
    }

    @Override
    public void markAsUnread(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new EntityNotFoundException(NotificationEntity.class);
        }
        Optional<NotificationEntity> notificationEntityOptional = notificationRepository.findById(id);
        NotificationEntity notificationEntity = notificationEntityOptional.orElseThrow(() -> new EntityNotFoundException(NotificationEntity.class));
        notificationEntity.setRead(false);
        notificationRepository.save(notificationEntity);
    }

    @Override
    public void markAllAsRead(Long receiverId) {
        List<NotificationEntity> notificationEntities = notificationRepository.findAllByUserEntityId(receiverId);

        for (NotificationEntity notificationEntity : notificationEntities) {
            notificationEntity.setRead(true);
            notificationRepository.save(notificationEntity);
        }
    }

    @Override
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new EntityNotFoundException(NotificationEntity.class);
        }

        notificationRepository.deleteById(id);
    }
}
