package com.web.controller;

import com.web.model.dto.ClassDTO;
import com.web.model.dto.EquipmentDTO;
import com.web.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;
    @GetMapping("/admin/getAllEquipment")
    public String getAllEquipment(Model model,  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,  @RequestParam(required = false) String name){
        Pageable pageable = PageRequest.of(page, size);
        Page<EquipmentDTO> equipmentPage = equipmentService.getAllEquipments(name, pageable);
        model.addAttribute("equipmentPage", equipmentPage);
        model.addAttribute("keyword", name);
        return "admin/equipment/list";
    }

    @GetMapping("/admin/addEquipment")
    public String addEquipment(Model model){
        return "admin/equipment/add";
    }

    @GetMapping("/admin/deleteEquipment")
    public String deleteEquipment(@RequestParam Long id){
        equipmentService.deleteEquipment(id);
        return "redirect:/admin/getAllEquipment";
    }
}
