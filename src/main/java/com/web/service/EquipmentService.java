package com.web.service;

import java.util.List;

import com.web.model.dto.ClassDTO;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EquipmentService {
    Page<EquipmentDTO> getAllEquipments(String name, Pageable pageable);
//    EquipmentResponse getEquipmentById(Long id);
//
//    List<EquipmentResponse> getAllEquipments();
//
//    List<EquipmentResponse> getEquipmentsByName(String name);
//
//    EquipmentResponse createEquipment(EquipmentDTO equipmentDTO);
//
//    EquipmentResponse updateEquipment(Long id, EquipmentDTO equipmentDTO);
//
//    void deleteEquipment(Long id);

    String addEquipment(EquipmentDTO equipmentDTO);
    String deleteEquipment(Long id);
}
