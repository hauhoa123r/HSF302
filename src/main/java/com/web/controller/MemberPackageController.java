package com.web.controller;

import com.web.service.MemberPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MemberPackageController {
    private MemberPackageService memberPackageService;

    @Autowired
    public void setMemberPackageService(MemberPackageService memberPackageService) {
        this.memberPackageService = memberPackageService;
    }


    @GetMapping("/admin/member/package/cancel/{memberId}")
    public String cancelMemberPackage(@PathVariable Long memberId) {
        memberPackageService.cancelMemberPackage(memberId);
        return "redirect:/admin/member";
    }
}
