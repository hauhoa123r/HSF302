package com.web.api;

import com.web.model.dto.ClassDTO;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import com.web.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/equipment")
public class EquipmentAPI {

    private EquipmentService equipmentService;

    @Autowired
    public void setEquipmentService(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

//    @GetMapping()
//    public List<EquipmentResponse> getAllEquipments() {
//        return equipmentService.getAllEquipments();
//    }

//    @GetMapping("/{id}")
//    public EquipmentResponse getEquipmentById(@PathVariable Long id) {
//        return equipmentService.getEquipmentById(id);
//    }
//
//    @GetMapping("/name/{name}")
//    public List<EquipmentResponse> getEquipmentsByName(@PathVariable String name) {
//        return equipmentService.getEquipmentsByName(name);
//    }
//
//    @PostMapping()
//    public EquipmentResponse createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
//        return equipmentService.createEquipment(equipmentDTO);
//    }
//
//    @PutMapping("/{id}")
//    public EquipmentResponse updateEquipment(@PathVariable Long id, @RequestBody EquipmentDTO equipmentDTO) {
//        return equipmentService.updateEquipment(id, equipmentDTO);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteEquipment(@PathVariable Long id) {
//        equipmentService.deleteEquipment(id);
//    }
    @PostMapping("/add")
    public ResponseEntity<?> addEquipment(@RequestBody EquipmentDTO dto) {
        equipmentService.addEquipment(dto);
        return ResponseEntity.ok("Thiết bị đã được thêm thành công!");
    }
}
