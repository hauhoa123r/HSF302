package com.web.service;

import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getNotificationsByUser(Long receiverId);

    NotificationResponse sendNotification(NotificationDTO notificationDTO);

    void markAsRead(Long id);

    void markAsUnread(Long id);

    void markAllAsRead(Long receiverId);

    void deleteNotification(Long id);
}
