package com.web.service;

import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getAllNotificationsByReceiverId(NotificationDTO notificationDTO);

    NotificationResponse sendNotification(NotificationDTO notificationDTO);

    void markAsRead(NotificationDTO notificationDTO);

    void markAsUnread(NotificationDTO notificationDTO);

    void markAllAsRead(NotificationDTO notificationDTO);

    void deleteNotification(NotificationDTO notificationDTO);
}
