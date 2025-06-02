package com.web.api;

import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;
import com.web.service.NutritionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/nutrition-plan")
public class NutritionPlanAPI {

    private NutritionPlanService nutritionPlanService;

    @Autowired
    public void setNutritionPlanService(NutritionPlanService nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @PostMapping
    public NutritionPlanResponse createNutritionPlan(@RequestBody NutritionPlanDTO nutritionPlanDTO) {
        return nutritionPlanService.createNutritionPlan(nutritionPlanDTO);
    }

    @GetMapping("/{id}")
    public NutritionPlanResponse getNutritionPlanById(@PathVariable Long id) {
        return nutritionPlanService.getNutritionPlanById(id);
    }

    @GetMapping("/member/{memberId}")
    public List<NutritionPlanResponse> getAllByMemberId(@PathVariable Long memberId) {
        return nutritionPlanService.getAllByMemberId(memberId);
    }

    @GetMapping("/member/{memberId}/date/{date}")
    public List<NutritionPlanResponse> getAllByMemberIdAndDate(@PathVariable Long memberId, @PathVariable Date date) {
        return nutritionPlanService.getAllByMemberIdAndDate(memberId, date);
    }

    @GetMapping("/member/{memberId}/week/{date}")
    public List<NutritionPlanResponse> getAllByMemberIdAndWeek(@PathVariable Long memberId, @PathVariable Date date) {
        return nutritionPlanService.getAllByMemberIdAndWeek(memberId, date);
    }

    @GetMapping("/member/{memberId}/month/{date}")
    public List<NutritionPlanResponse> getAllByMemberIdAndMonth(@PathVariable Long memberId, @PathVariable Date date) {
        return nutritionPlanService.getAllByMemberIdAndMonth(memberId, date);
    }

    @PutMapping("/{id}")
    public NutritionPlanResponse updateNutritionPlan(@PathVariable Long id, @RequestBody NutritionPlanDTO nutritionPlanDTO) {
        return nutritionPlanService.updateNutritionPlan(id, nutritionPlanDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteNutritionPlan(@PathVariable Long id) {
        nutritionPlanService.deleteNutritionPlan(id);
    }
}
