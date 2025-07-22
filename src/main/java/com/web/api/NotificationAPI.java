package com.web.api;

import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotificationAPI {

    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/api/user/notification/{receiverId}")
    public List<NotificationResponse> getAllNotifications(@PathVariable Long receiverId) {
        return notificationService.getNotificationsByUser(receiverId);
    }

    @PutMapping("/api/user/notification/read/{id}")
    public boolean markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return true;
    }

    @PutMapping("/api/user/notification/unread/{id}")
    public boolean markAsUnread(@PathVariable Long id) {
        notificationService.markAsUnread(id);
        return true;
    }

    @PutMapping("/api/user/notification/read-all/member/{receiverId}")
    public boolean markAllAsRead(@PathVariable Long receiverId) {
        notificationService.markAllAsRead(receiverId);
        return true;
    }

    @PostMapping("/api/admin/notification")
    public NotificationResponse sendNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.sendNotification(notificationDTO);
    }

    @PostMapping("/api/admin/notification/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}
