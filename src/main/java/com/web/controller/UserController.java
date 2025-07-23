package com.web.controller;

import com.web.converter.PackageConverter;
import com.web.model.response.PackageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private PackageConverter packageConverter;

    @GetMapping("/login")
    public ModelAndView login(Model model) {
        ModelAndView mav = new ModelAndView("shared/login");
        return mav;
    }

    @GetMapping("/register")
    public ModelAndView register(Model model) {
        ModelAndView mav = new ModelAndView("shared/register");
        List<PackageResponse> packages = packageConverter.toConverterPackageResponse();
        mav.addObject("packages", packages);
        return mav;
    }

    @GetMapping("/forgot/password")
    public ModelAndView forgotPassword(Model model) {
        ModelAndView mav = new ModelAndView("shared/forgot-password");
        return mav;
    }

    @GetMapping("/reset/password")
    public ModelAndView resetPassword(Model model) {
        ModelAndView mav = new ModelAndView("shared/reset-password");
        return mav;
    }

}

