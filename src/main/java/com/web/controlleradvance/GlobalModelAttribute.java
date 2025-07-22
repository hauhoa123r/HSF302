package com.web.controlleradvance;

import com.web.model.response.NotificationResponse;
import com.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttribute {
    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute(name = "notifications")
    public List<NotificationResponse> getNotifications() {
        // TODO: Replace with real user id from session or security context
        Long userId = 1L;
        return notificationService.getNotificationsByUser(userId);
    }

}
