package com.web.controller;

import com.web.model.response.AttendanceResponse;
import com.web.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AttendanceController {
    private AttendanceService attendanceService;

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/admin/member/checkin/{memberId}")
    public String checkInMember(@PathVariable Long memberId) {
        attendanceService.checkInMember(memberId);
        return "redirect:/admin/member";
    }

    @GetMapping("/admin/member/checkout/{memberId}")
    public String checkOutMember(@PathVariable Long memberId) {
        attendanceService.checkOutMember(memberId);
        return "redirect:/admin/member";
    }

    @GetMapping("/admin/member/attendance")
    public String showAttendancePage(ModelMap modelMap) {
        modelMap.put("attendance", new AttendanceResponse());
        return "admin/members/attendance";
    }
}
