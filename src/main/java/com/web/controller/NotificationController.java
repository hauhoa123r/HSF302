package com.web.controller;

import com.web.model.dto.NotificationDTO;
import com.web.model.response.NotificationResponse;
import com.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    private NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/getAll")
    public ModelAndView getAllNotifications(@ModelAttribute NotificationDTO notificationDTO) {
        ModelAndView modelAndView = new ModelAndView();
        List<NotificationResponse> notifications = notificationService.getAllNotificationsByReceiverId(notificationDTO);
        modelAndView.addObject("notifications", notifications);
        modelAndView.setViewName("notifications");
        return modelAndView;
    }

    @GetMapping("/read")
    public ModelAndView markAsRead(@ModelAttribute NotificationDTO notificationDTO) {
        notificationService.markAsRead(notificationDTO);
        return new ModelAndView("redirect:/notification/getAll");
    }
}
