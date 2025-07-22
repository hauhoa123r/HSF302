package com.web.controller;

import com.web.service.*;
import com.web.utils.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private MemberService memberService;
    private PaymentService paymentService;
    private ClassService classService;
    private TrainerService trainerService;
    private MemberPackageService memberPackageService;
    private AttendanceService attendanceService;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Autowired
    public void setClassService(ClassService classService) {
        this.classService = classService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setMemberPackageService(MemberPackageService memberPackageService) {
        this.memberPackageService = memberPackageService;
    }

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/admin")
    public String adminHome(ModelMap modelMap) {
        modelMap.put("memberCount", memberService.countMembers());
        modelMap.put("totalRevenueThisMonth", NumberUtils.shortenCurrency(paymentService.getRevenueThisMonth().toString()));
        modelMap.put("classCount", classService.countClasses());
        modelMap.put("trainerCount", trainerService.countTrainers());
        modelMap.put("revenueMap", paymentService.getRevenues6MonthsAgo());
        modelMap.put("packageCountMap", memberPackageService.getPackageCountByName());
        modelMap.put("attendanceCountMap", attendanceService.getAttendanceCountByTimeToday());
        return "admin/index";
    }
}
