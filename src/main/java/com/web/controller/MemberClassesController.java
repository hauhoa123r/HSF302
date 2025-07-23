package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberClassesController {

    @GetMapping("/member/classes/list")
    public ModelAndView showMemberClassesPage() {
        ModelAndView mv = new ModelAndView("member/classes/list");
        return mv;
    }
}
