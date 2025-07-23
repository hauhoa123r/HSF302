package com.web.service;

import com.web.model.dto.EquipmentDTO;
import com.web.model.dto.PromotionsDTO;
import com.web.model.response.PromotionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PromotionService {
    List<PromotionResponse> getPromotionsByPackage(Long packageId);
    Page<PromotionsDTO> getAllPromotions(String name, Pageable pageable);
    String deletePromotion(Long id);
}
