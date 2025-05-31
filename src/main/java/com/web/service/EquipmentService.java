package com.web.service;

import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;

import java.util.List;

public interface EquipmentService {
    EquipmentResponse getEquipmentById(EquipmentDTO equipmentDTO);

    List<EquipmentResponse> getAllEquipments();

    List<EquipmentResponse> getEquipmentsByName(EquipmentDTO equipmentDTO);

    EquipmentResponse createEquipment(EquipmentDTO equipmentDTO);

    EquipmentResponse updateEquipment(EquipmentDTO equipmentDTO);

    void deleteEquipment(EquipmentDTO equipmentDTO);

}
