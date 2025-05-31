package com.web.service;

import com.web.entity.EquipmentEntity;
import com.web.exception.ResourceNotFoundException;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import com.web.repository.EquipmentRepository;
import com.web.service.impl.EquipmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class EquipmentServiceUnitTest {
    @Autowired
    private EquipmentServiceImpl equipmentService;
    @MockBean
    private EquipmentRepository equipmentRepositoryMock;

    // --- getAllEquipments ---
    @Test
    void getAllEquipments_success() {
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(1L);
        Mockito.when(equipmentRepositoryMock.findAll()).thenReturn(List.of(entity));
        List<EquipmentResponse> result = equipmentService.getAllEquipments();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
    }

    // --- createEquipment ---
    @Test
    void createEquipment_success() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(1L);
        dto.setName("Máy chạy bộ");
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(1L);
        Mockito.when(equipmentRepositoryMock.existsById(1L)).thenReturn(false);
        Mockito.when(equipmentRepositoryMock.save(Mockito.any(EquipmentEntity.class))).thenReturn(entity);
        EquipmentResponse res = equipmentService.createEquipment(dto);
        Assertions.assertEquals(1L, res.getId());
    }

    @Test
    void createEquipment_duplicate() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(2L);
        dto.setName("Tạ đơn");
        Mockito.when(equipmentRepositoryMock.existsById(2L)).thenReturn(true);
        Assertions.assertThrows(EntityAlreadyExistException.class, () -> equipmentService.createEquipment(dto));
    }

    @Test
    void createEquipment_nullName() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(3L);
        dto.setName(null);
        Assertions.assertThrows(Exception.class, () -> equipmentService.createEquipment(dto));
    }

    // --- updateEquipment ---
    @Test
    void updateEquipment_success() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(1L);
        dto.setName("Máy chạy bộ mới");
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(1L);
        Mockito.when(equipmentRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(equipmentRepositoryMock.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(equipmentRepositoryMock.save(Mockito.any(EquipmentEntity.class))).thenReturn(entity);
        EquipmentResponse res = equipmentService.updateEquipment(dto);
        Assertions.assertEquals(1L, res.getId());
    }

    @Test
    void updateEquipment_notFound() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(999L);
        Mockito.when(equipmentRepositoryMock.existsById(999L)).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> equipmentService.updateEquipment(dto));
    }

    // --- deleteEquipment ---
    @Test
    void deleteEquipment_success() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(1L);
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(1L);
        Mockito.when(equipmentRepositoryMock.existsById(1L)).thenReturn(true);
        Mockito.when(equipmentRepositoryMock.findById(1L)).thenReturn(Optional.of(entity));
        equipmentService.deleteEquipment(dto);
        Mockito.verify(equipmentRepositoryMock).deleteById(1L);
    }

    @Test
    void deleteEquipment_nullId() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(null);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> equipmentService.deleteEquipment(dto));
    }

    @Test
    void deleteEquipment_notFound() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(999L);
        Mockito.when(equipmentRepositoryMock.existsById(999L)).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class, () -> equipmentService.deleteEquipment(dto));
    }

    // --- getEquipmentsByName ---
    @Test
    void getEquipmentsByName_success() {
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(1L);
        entity.setName("Máy chạy bộ");
        Mockito.when(equipmentRepositoryMock.findByNameContainingIgnoreCase("bộ")).thenReturn(List.of(entity));
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("bộ");
        List<EquipmentResponse> result = equipmentService.getEquipmentsByName(dto);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Máy chạy bộ", result.get(0).getName());
    }

    @Test
    void getEquipmentsByName_notFound() {
        Mockito.when(equipmentRepositoryMock.findByNameContainingIgnoreCase("khongton@tai123456"))
                .thenReturn(List.of());
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("khongton@tai123456");
        Assertions.assertThrows(EntityNotFoundException.class, () -> equipmentService.getEquipmentsByName(dto));
    }

    // --- getEquipmentById ---
    @Test
    void getEquipmentById_success() {
        EquipmentEntity entity = new EquipmentEntity();
        entity.setId(1L);
        Mockito.when(equipmentRepositoryMock.findById(1L)).thenReturn(Optional.of(entity));
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(1L);
        EquipmentResponse result = equipmentService.getEquipmentById(dto);
        Assertions.assertEquals(1L, result.getId());
    }

    @Test
    void getEquipmentById_notFound() {
        Mockito.when(equipmentRepositoryMock.findById(999999L)).thenReturn(Optional.empty());
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(999999L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> equipmentService.getEquipmentById(dto));
    }
}