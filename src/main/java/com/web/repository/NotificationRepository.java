package com.web.repository;

import com.web.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findNotificationEntitiesByReceiverId(Long receiverId);
}
