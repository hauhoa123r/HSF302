package com.web.controller;

import com.web.converter.MemberConverter;
import com.web.model.response.MemberHomePageResponse;
import com.web.model.response.MemberResponse;
import com.web.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberController {

    private MemberService memberService;
    private MemberPackageService memberPackageService;
    private PackageService packageService;
    private TrainerService trainerService;
    private PromotionService promotionService;

    @Autowired
    private MemberConverter memberConverter;

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setMemberPackageService(MemberPackageService memberPackageService) {
        this.memberPackageService = memberPackageService;
    }

    @Autowired
    public void setPackageService(PackageService packageService) {
        this.packageService = packageService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setPromotionService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/admin/member")
    public String showMemberPageForAdmin(ModelMap modelMap) {
        modelMap.put("packages", packageService.getAllPackages());
        return "admin/members/list";
    }

//    @GetMapping("/admin/member/add")
//    public String showMemberAddPage(ModelMap modelMap) {
//        modelMap.put("trainers", trainerService.getTrainers());
//        modelMap.put("packages", packageService.getAllPackages());
//        modelMap.put("member", new MemberResponse());
//        return "admin/members/add";
//    }

    @GetMapping("/admin/member/detail/{memberId}")
    public String showMemberDetail(@PathVariable Long memberId, ModelMap modelMap) {
        modelMap.put("member", memberService.getMember(memberId));
        return "admin/members/detail";
    }


    @GetMapping("/admin/member/update/{memberId}")
    public String showMemberEditPage(@PathVariable Long memberId, ModelMap modelMap) {
        MemberResponse memberResponse = memberService.getMember(memberId);
        modelMap.put("member", memberResponse);
        if (memberResponse.getMemberPackageEntity() != null) {
            modelMap.put("promotions", promotionService.getPromotionsByPackage(memberResponse.getMemberPackageEntity().getPackageEntity().getId()));
        }
        return "admin/members/edit";
    }

    @GetMapping("/admin/member/delete/{memberId}")
    public String deleteMember(@PathVariable Long memberId, ModelMap modelMap) {
        memberService.deleteMember(memberId);
        modelMap.put("message", "Member deleted successfully.");
        return "redirect:/admin/member";
    }

    @GetMapping("/admin/member/notify/{userId}")
    public String notifyMember(@PathVariable Long userId, ModelMap modelMap) {
        modelMap.addAttribute("userId", userId);
        return "admin/members/notify";
    }


    @GetMapping("/admin/member/package/register/{memberId}")
    public String showMemberPackageRegisterPage(@PathVariable Long memberId, ModelMap modelMap) {
        modelMap.put("member", memberService.getMember(memberId));
        modelMap.put("packages", packageService.getAllPackages());
        return "admin/members/register-package";
    }

    @GetMapping("/member/homepage")
    public ModelAndView viewHomePage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("member/index");
        Long userId = (Long) request.getSession().getAttribute("userId");
        MemberHomePageResponse memberHomePageResponse = memberConverter.toHomePageResponse(userId);
        mv.addObject("userInfo", memberHomePageResponse);
        return mv;
    }
}
