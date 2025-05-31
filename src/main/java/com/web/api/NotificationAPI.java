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

    @GetMapping("/getAll")
    public List<NotificationResponse> getAllNotifications(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.getAllNotificationsByReceiverId(notificationDTO);
    }

    @GetMapping("/read")
    public boolean markAsRead(@RequestBody NotificationDTO notificationDTO) {
        notificationService.markAsRead(notificationDTO);
        return true;
    }

    @PostMapping("/send")
    public NotificationResponse sendNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.sendNotification(notificationDTO);
    }

    @PostMapping("/delete")
    public void deleteNotification(@RequestBody NotificationDTO notificationDTO) {
        notificationService.deleteNotification(notificationDTO);
    }
}
