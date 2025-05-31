package com.web.api;

import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import com.web.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentAPI {

    private EquipmentService equipmentService;

    @Autowired
    public void setEquipmentService(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("/getAll")
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentService.getAllEquipments();
    }

    @GetMapping("/getById")
    public EquipmentResponse getEquipmentById(@RequestBody EquipmentDTO equipmentDTO) {
        return equipmentService.getEquipmentById(equipmentDTO);
    }

    @GetMapping("/getByName")
    public List<EquipmentResponse> getEquipmentsByName(@RequestBody EquipmentDTO equipmentDTO) {
        return equipmentService.getEquipmentsByName(equipmentDTO);
    }

    @PostMapping("/create")
    public EquipmentResponse createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        return equipmentService.createEquipment(equipmentDTO);
    }

    @PostMapping("/update")
    public EquipmentResponse updateEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        return equipmentService.updateEquipment(equipmentDTO);
    }

    @PostMapping("/delete")
    public void deleteEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        equipmentService.deleteEquipment(equipmentDTO);
    }
}
