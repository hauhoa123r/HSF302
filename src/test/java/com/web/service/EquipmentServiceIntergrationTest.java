package com.web.service;

import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EquipmentServiceIntergrationTest {
    @Autowired
    private EquipmentService equipmentService;

    @Test
    void getAllEquipments_success() {
        List<EquipmentResponse> result = equipmentService.getAllEquipments();
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void createEquipment_and_deleteEquipment_success() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Thiết bị test");
        EquipmentResponse created = equipmentService.createEquipment(dto);
        Assertions.assertNotNull(created.getId());
        // Xóa
        EquipmentDTO delDto = new EquipmentDTO();
        delDto.setId(created.getId());
        equipmentService.deleteEquipment(delDto);
        Assertions.assertThrows(Exception.class, () -> equipmentService.deleteEquipment(delDto));
    }

    @Test
    void updateEquipment_notFound() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(99999L);
        dto.setName("Không tồn tại");
        Assertions.assertThrows(Exception.class, () -> equipmentService.updateEquipment(dto));
    }

    @Test
    void createEquipment_nullName() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName(null);
        Assertions.assertThrows(Exception.class, () -> equipmentService.createEquipment(dto));
    }

    @Test
    void createEquipment_duplicate() {
        // Tạo mới thiết bị
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Máy chạy bộ");
        EquipmentResponse created = equipmentService.createEquipment(dto);
        // Thử tạo lại với cùng tên/id (nếu service kiểm tra duplicate theo id hoặc
        // name)
        EquipmentDTO duplicateDto = new EquipmentDTO();
        duplicateDto.setId(created.getId());
        duplicateDto.setName("Máy chạy bộ");
        Assertions.assertThrows(Exception.class, () -> equipmentService.createEquipment(duplicateDto));
        // Xóa thiết bị test
        EquipmentDTO delDto = new EquipmentDTO();
        delDto.setId(created.getId());
        equipmentService.deleteEquipment(delDto);
    }

    @Test
    void updateEquipment_success() {
        // Tạo mới thiết bị
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Thiết bị update");
        EquipmentResponse created = equipmentService.createEquipment(dto);
        // Cập nhật
        EquipmentDTO updateDto = new EquipmentDTO();
        updateDto.setId(created.getId());
        updateDto.setName("Thiết bị đã cập nhật");
        EquipmentResponse updated = equipmentService.updateEquipment(updateDto);
        Assertions.assertEquals("Thiết bị đã cập nhật", updated.getName());
        // Xóa thiết bị test
        EquipmentDTO delDto = new EquipmentDTO();
        delDto.setId(created.getId());
        equipmentService.deleteEquipment(delDto);
    }

    @Test
    void deleteEquipment_notFound() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(999999L);
        Assertions.assertThrows(Exception.class, () -> equipmentService.deleteEquipment(dto));
    }

    @Test
    void getEquipmentById_success() {
        // Tạo mới thiết bị
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Thiết bị getById");
        EquipmentResponse created = equipmentService.createEquipment(dto);
        EquipmentDTO getDto = new EquipmentDTO();
        getDto.setId(created.getId());
        EquipmentResponse found = equipmentService.getEquipmentById(getDto);
        Assertions.assertEquals(created.getId(), found.getId());
        // Xóa thiết bị test
        EquipmentDTO delDto = new EquipmentDTO();
        delDto.setId(created.getId());
        equipmentService.deleteEquipment(delDto);
    }

    @Test
    void getEquipmentById_notFound() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(999999L);
        Assertions.assertThrows(Exception.class, () -> equipmentService.getEquipmentById(dto));
    }

    @Test
    void getEquipmentsByName_success() {
        // Tạo mới thiết bị
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Thiết bị tìm kiếm");
        equipmentService.createEquipment(dto);
        EquipmentDTO searchDto = new EquipmentDTO();
        searchDto.setName("tìm kiếm");
        List<EquipmentResponse> found = equipmentService.getEquipmentsByName(searchDto);
        Assertions.assertFalse(found.isEmpty());
        // Không xóa để test nhiều lần
    }

    @Test
    void getEquipmentsByName_notFound() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("khongton@tai123456");
        Assertions.assertThrows(Exception.class, () -> equipmentService.getEquipmentsByName(dto));
    }
}