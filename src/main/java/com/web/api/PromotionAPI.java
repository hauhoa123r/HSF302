package com.web.api;

import com.web.model.response.PromotionResponse;
import com.web.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PromotionAPI {
    private PromotionService promotionService;

    @Autowired
    public void setPromotionService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/api/promotion/package/{packageId}")
    public List<PromotionResponse> getPromotionsByPackageId(@PathVariable Long packageId) {
        return promotionService.getPromotionsByPackage(packageId);
    }
}
