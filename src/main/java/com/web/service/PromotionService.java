package com.web.service;

import com.web.model.response.PromotionResponse;

import java.util.List;

public interface PromotionService {
    List<PromotionResponse> getPromotionsByPackage(Long packageId);
}
