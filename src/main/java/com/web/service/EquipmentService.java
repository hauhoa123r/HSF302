package com.web.service;

import java.util.List;

import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;

public interface EquipmentService {
    EquipmentResponse getEquipmentById(Long id);

    List<EquipmentResponse> getAllEquipments();

    List<EquipmentResponse> getEquipmentsByName(String name);

    EquipmentResponse createEquipment(EquipmentDTO equipmentDTO);

    EquipmentResponse updateEquipment(Long id, EquipmentDTO equipmentDTO);

    void deleteEquipment(Long id);

}
