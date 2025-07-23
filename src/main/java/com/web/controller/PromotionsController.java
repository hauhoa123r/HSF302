package com.web.controller;

import com.web.model.dto.PromotionsDTO;
import com.web.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PromotionsController {
    @Autowired
    private PromotionService promotionService;
    @GetMapping("/admin/getAllPromotions")
    public String getAllEquipment(Model model, @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String name){
        Pageable pageable = PageRequest.of(page, size);
        Page<PromotionsDTO> promotionsDTOPage = promotionService.getAllPromotions(name, pageable);
        model.addAttribute("promotionsPage", promotionsDTOPage);
        model.addAttribute("keyword", name);
        return "admin/promotions/list";
    }

    @GetMapping("/admin/addPromotion")
    public String addPromotion(){
        return "admin/promotions/add";
    }

    @GetMapping("/admin/deletePromotion")
    public String deletePromotion(@RequestParam Long id){
        promotionService.deletePromotion(id);
        return "redirect:/admin/getAllPromotions";
    }
}
