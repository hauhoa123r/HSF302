package com.web.api;

import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationAPI {

    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/member/{receiverId}")
    public List<NotificationResponse> getAllNotifications(@PathVariable Long receiverId) {
        return notificationService.getAllNotificationsByReceiverId(receiverId);
    }

    @PutMapping("/read/{id}")
    public boolean markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return true;
    }

    @PutMapping("/unread/{id}")
    public boolean markAsUnread(@PathVariable Long id) {
        notificationService.markAsUnread(id);
        return true;
    }

    @PutMapping("/read-all/member/{receiverId}")
    public boolean markAllAsRead(@PathVariable Long receiverId) {
        notificationService.markAllAsRead(receiverId);
        return true;
    }

    @PostMapping()
    public NotificationResponse sendNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.sendNotification(notificationDTO);
    }

    @PostMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}
