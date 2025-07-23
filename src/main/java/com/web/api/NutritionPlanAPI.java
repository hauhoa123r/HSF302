package com.web.api;

import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;
import com.web.service.NutritionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trainer/nutrition-plan")
public class NutritionPlanAPI {

    private NutritionPlanService nutritionPlanService;

    @Autowired
    public void setNutritionPlanService(NutritionPlanService nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> saveNutritionPlan(@RequestBody NutritionPlanDTO nutritionPlanDTO) {
        boolean success = nutritionPlanService.saveNutritionPlan(nutritionPlanDTO);
        if (success) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Tạo kế hoạch dinh dưỡng thành công.");
            body.put("success", true);
            return ResponseEntity.ok(body);
        } else {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Tạo kế hoạch dinh dưỡng thất bại.");
            body.put("success", false);
            return ResponseEntity.badRequest().body(body);
        }
    }
}
