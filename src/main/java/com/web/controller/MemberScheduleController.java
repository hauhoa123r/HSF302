package com.web.controller;

import com.web.converter.MemberConverter;
import com.web.converter.RegisterClassConverter;
import com.web.model.response.MemberHomePageResponse;
import com.web.model.response.RegisterClassResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller()
public class MemberScheduleController {

    @Autowired
    private MemberConverter memberConverter;

    @Autowired
    private RegisterClassConverter registerClassConverter;

    @GetMapping("/member/schedule/view")
    public ModelAndView viewSchdule() {
        ModelAndView mv = new ModelAndView("member/schedule/view");
        return mv;
    }

    @GetMapping("/member/schedule/manage")
    public ModelAndView viewManageSchedule(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("member/schedule/manage");
        Long userId = (Long) request.getSession().getAttribute("userId");
        MemberHomePageResponse memberHomePageResponse = memberConverter.toHomePageResponse(userId);
        mv.addObject("userInfo", memberHomePageResponse);
        List<RegisterClassResponse> classes = registerClassConverter.toConverterRegisterClassResponse();
        mv.addObject("classes", classes);
        return mv;
    }
}
