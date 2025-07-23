package com.web.controller;

import com.web.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TrainerController {

    private TrainerService trainerService;

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/admin/trainer")
    public String showPageTrainerListForAdmin(ModelMap modelmap) {
        modelmap.addAttribute("specializations", trainerService.getAllSpecializations());
        return "admin/trainers/list";
    }

    @GetMapping("/admin/trainer/detail/{id}")
    public String showPageDetailTrainerForAdmin(@PathVariable Long id, ModelMap modelmap) {
        modelmap.addAttribute("trainer", trainerService.getTrainerById(id));
        return "admin/trainers/detail";
    }

    @GetMapping("/admin/trainer/add")
    public String showPageAddTrainerForAdmin(ModelMap modelmap) {
        modelmap.addAttribute("specializations", trainerService.getAllSpecializations());
        return "admin/trainers/add";
    }

    @GetMapping("/admin/trainer/edit/{id}")
    public String showPageEditTrainerForAdmin(@PathVariable Long id, ModelMap modelmap) {
        modelmap.addAttribute("trainer", trainerService.getTrainerById(id));
        modelmap.addAttribute("specializations", trainerService.getAllSpecializations());
        return "admin/trainers/edit";
    }

    @GetMapping("/admin/trainer/delete/{id}")
    public String showPageDeleteTrainerForAdmin(@PathVariable Long id, ModelMap modelmap) {
        modelmap.addAttribute("trainer", trainerService.getTrainerById(id));
        return "admin/trainers/delete";
    }
}
