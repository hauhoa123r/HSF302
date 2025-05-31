package com.web.repository;

import com.web.entity.EquipmentEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void findByName() {
        EquipmentEntity equipmentEntityExpected = new EquipmentEntity(1L, "Máy chạy bộ", "Cardio", "Hoạt động", Date.valueOf("2025-05-01"));

        List<EquipmentEntity> equipmentEntitiesActual = equipmentRepository.findByNameContainingIgnoreCase("bộ");

        assertNotNull(equipmentEntitiesActual, "The list should not be null");
        assertEquals(1, equipmentEntitiesActual.size(), "The list should contain one element");

        EquipmentEntity equipmentEntityActual = equipmentEntitiesActual.get(0);

        assertNotNull(equipmentEntityActual, "The equipment should not be null");
        assertEquals(equipmentEntityExpected.getName(), equipmentEntityActual.getName());
        assertEquals(equipmentEntityExpected.getType(), equipmentEntityActual.getType());
        assertEquals(equipmentEntityExpected.getStatus(), equipmentEntityActual.getStatus());
        assertEquals(equipmentEntityExpected.getLastMaintenance(), equipmentEntityActual.getLastMaintenance());
    }
}
