package com.web.controller;

import com.web.model.response.MemberOfTrainerResponse;
import com.web.model.response.NutritionPlanDetailResponse;
import com.web.model.response.NutritionPlanResponsePlus;
import com.web.service.NutritionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/trainer")
public class NutritionController {

    private NutritionPlanService nutritionPlanService;

    @Autowired
    public void setNutritionPlanService(NutritionPlanService nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @GetMapping("/create/{trainerId}")
    public String openCreateNutritionPlan(@PathVariable Long trainerId, Model model) {
        List<MemberOfTrainerResponse> memberResponse = nutritionPlanService.getMemberOfTrainer(trainerId);

        model.addAttribute("memberResponse", memberResponse);
        model.addAttribute("trainerId", trainerId);

        return "trainer/nutrition/create";
    }

    @GetMapping("/nutrition-plan/{trainerId}")
    public String openNutritionPlan(@PathVariable Long trainerId, Model model) {
        List<NutritionPlanResponsePlus> nutritionPlanResponsePluses = nutritionPlanService.getNutritionPlanByTrainerId(trainerId);

        model.addAttribute("nutritionPlanList", nutritionPlanResponsePluses);
        model.addAttribute("trainerId", trainerId);

        return "trainer/nutrition/nutrition";
    }

    @GetMapping("/nutrition-plan/detail/{planId}")
    public String openNutritionPlanDetail(@PathVariable Long planId, Model model) {
        NutritionPlanResponsePlus nutritionPlanResponsePlus = nutritionPlanService.getNutritionPlanById(planId);
        List<NutritionPlanDetailResponse> nutritionPlanDetailResponses = nutritionPlanService.getNutritionPlanDetailByPlanId(planId);

        model.addAttribute("nutritionPlanDetail", nutritionPlanDetailResponses);
        model.addAttribute("nutritionPlan", nutritionPlanResponsePlus);

        return "trainer/nutrition/detail";
    }

    @GetMapping("/nutrition-plan/edit/{planId}")
    public String openNutritionPlanEdit(@PathVariable Long planId, Model model) {
        NutritionPlanResponsePlus nutritionPlanResponsePlus = nutritionPlanService.getNutritionPlanById(planId);

        model.addAttribute("nutritionPlan", nutritionPlanResponsePlus);

        return "trainer/nutrition/edit";
    }
}
