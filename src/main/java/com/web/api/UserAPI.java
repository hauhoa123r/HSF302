package com.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/submit")
public class UserAPI {

    @PostMapping
    public String handleSubmit(@RequestParam String id,
                               @RequestParam String name,
                               @RequestParam int age,
                               Model model) {
        model.addAttribute("msg", "Đã nhận dữ liệu: " + id + ", " + name + ", " + age);
        return "object/result";
    }
}
