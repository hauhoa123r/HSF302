package com.web.service;

import com.web.entity.EquipmentEntity;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import com.web.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Equipment Service Integration Tests")
class EquipmentServiceIntegrationTest {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private EquipmentDTO testEquipmentDTO;

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        equipmentRepository.deleteAll();

        // Setup test data
        testEquipmentDTO = new EquipmentDTO();
        testEquipmentDTO.setName("Test Equipment");
        testEquipmentDTO.setType("Test Type");
        testEquipmentDTO.setStatus("Available");
        testEquipmentDTO.setLastMaintenance(Date.valueOf(LocalDate.now().minusDays(15)));
    }

    @Test
    @DisplayName("Should create and retrieve equipment successfully")
    void testCreateAndGetEquipment_Success() {
        // Given & When - Create equipment
        EquipmentResponse createdEquipment = equipmentService.createEquipment(testEquipmentDTO);

        // Then - Verify creation
        assertNotNull(createdEquipment);
        assertNotNull(createdEquipment.getId());
        assertEquals(testEquipmentDTO.getName(), createdEquipment.getName());
        assertEquals(testEquipmentDTO.getType(), createdEquipment.getType());
        assertEquals(testEquipmentDTO.getStatus(), createdEquipment.getStatus());

        // When - Retrieve by ID
        EquipmentResponse retrievedEquipment = equipmentService.getEquipmentById(createdEquipment.getId());

        // Then - Verify retrieval
        assertNotNull(retrievedEquipment);
        assertEquals(createdEquipment.getId(), retrievedEquipment.getId());
        assertEquals(createdEquipment.getName(), retrievedEquipment.getName());
        assertEquals(createdEquipment.getType(), retrievedEquipment.getType());
        assertEquals(createdEquipment.getStatus(), retrievedEquipment.getStatus());
    }

    @Test
    @DisplayName("Should get all equipments successfully")
    void testGetAllEquipments_Success() {
        // Given - Create multiple equipments
        EquipmentDTO equipment1DTO = new EquipmentDTO();
        equipment1DTO.setName("Máy chạy bộ");
        equipment1DTO.setType("Cardio");
        equipment1DTO.setStatus("Available");

        EquipmentDTO equipment2DTO = new EquipmentDTO();
        equipment2DTO.setName("Tạ đơn");
        equipment2DTO.setType("Weight");
        equipment2DTO.setStatus("Available");

        EquipmentResponse created1 = equipmentService.createEquipment(equipment1DTO);
        EquipmentResponse created2 = equipmentService.createEquipment(equipment2DTO);

        // When
        List<EquipmentResponse> allEquipments = equipmentService.getAllEquipments();

        // Then
        assertNotNull(allEquipments);
        assertEquals(2, allEquipments.size());

        boolean found1 = allEquipments.stream().anyMatch(e -> e.getId().equals(created1.getId()));
        boolean found2 = allEquipments.stream().anyMatch(e -> e.getId().equals(created2.getId()));

        assertTrue(found1, "First equipment should be found in the list");
        assertTrue(found2, "Second equipment should be found in the list");
    }

    @Test
    @DisplayName("Should get equipments by name successfully")
    void testGetEquipmentsByName_Success() {
        // Given - Create equipments with specific names
        EquipmentDTO cardioEquipment = new EquipmentDTO();
        cardioEquipment.setName("Máy chạy bộ điện");
        cardioEquipment.setType("Cardio");
        cardioEquipment.setStatus("Available");

        EquipmentDTO weightEquipment = new EquipmentDTO();
        weightEquipment.setName("Tạ đơn 10kg");
        weightEquipment.setType("Weight");
        weightEquipment.setStatus("Available");

        equipmentService.createEquipment(cardioEquipment);
        equipmentService.createEquipment(weightEquipment);

        // When - Search by partial name
        List<EquipmentResponse> cardioResults = equipmentService.getEquipmentsByName("máy");
        List<EquipmentResponse> weightResults = equipmentService.getEquipmentsByName("tạ");

        // Then
        assertNotNull(cardioResults);
        assertEquals(1, cardioResults.size());
        assertTrue(cardioResults.get(0).getName().toLowerCase().contains("máy"));

        assertNotNull(weightResults);
        assertEquals(1, weightResults.size());
        assertTrue(weightResults.get(0).getName().toLowerCase().contains("tạ"));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when searching non-existent equipment by name")
    void testGetEquipmentsByName_NotFound() {
        // Given - No equipment with this name exists
        String nonExistentName = "NonExistentEquipment";

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.getEquipmentsByName(nonExistentName);
        });
    }

    @Test
    @DisplayName("Should update equipment successfully")
    void testUpdateEquipment_Success() {
        // Given - Create initial equipment
        EquipmentResponse createdEquipment = equipmentService.createEquipment(testEquipmentDTO);

        // Prepare update data
        EquipmentDTO updateDTO = new EquipmentDTO();
        updateDTO.setName("Updated Equipment Name");
        updateDTO.setType("Updated Type");
        updateDTO.setStatus("Maintenance");
        updateDTO.setLastMaintenance(Date.valueOf(LocalDate.now()));

        // When - Update equipment
        EquipmentResponse updatedEquipment = equipmentService.updateEquipment(createdEquipment.getId(), updateDTO);

        // Then - Verify update
        assertNotNull(updatedEquipment);
        assertEquals(createdEquipment.getId(), updatedEquipment.getId());
        assertEquals(updateDTO.getName(), updatedEquipment.getName());
        assertEquals(updateDTO.getType(), updatedEquipment.getType());
        assertEquals(updateDTO.getStatus(), updatedEquipment.getStatus());

        // Verify in database
        EquipmentResponse retrievedEquipment = equipmentService.getEquipmentById(createdEquipment.getId());
        assertEquals(updateDTO.getName(), retrievedEquipment.getName());
        assertEquals(updateDTO.getType(), retrievedEquipment.getType());
        assertEquals(updateDTO.getStatus(), retrievedEquipment.getStatus());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent equipment")
    void testUpdateEquipment_NotFound() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.updateEquipment(nonExistentId, testEquipmentDTO);
        });
    }

    @Test
    @DisplayName("Should delete equipment successfully")
    void testDeleteEquipment_Success() {
        // Given - Create equipment
        EquipmentResponse createdEquipment = equipmentService.createEquipment(testEquipmentDTO);
        Long equipmentId = createdEquipment.getId();

        // Verify equipment exists
        assertDoesNotThrow(() -> {
            equipmentService.getEquipmentById(equipmentId);
        });

        // When - Delete equipment
        assertDoesNotThrow(() -> {
            equipmentService.deleteEquipment(equipmentId);
        });

        // Then - Verify equipment is deleted
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.getEquipmentById(equipmentId);
        });

        // Verify in repository
        Optional<EquipmentEntity> deletedEquipment = equipmentRepository.findById(equipmentId);
        assertFalse(deletedEquipment.isPresent(), "Equipment should not exist in database");
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existent equipment")
    void testDeleteEquipment_NotFound() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.deleteEquipment(nonExistentId);
        });
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when getting non-existent equipment by ID")
    void testGetEquipmentById_NotFound() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.getEquipmentById(nonExistentId);
        });
    }

    @Test
    @DisplayName("Should handle case-insensitive name search")
    void testGetEquipmentsByName_CaseInsensitive() {
        // Given - Create equipment with mixed case name
        EquipmentDTO mixedCaseEquipment = new EquipmentDTO();
        mixedCaseEquipment.setName("MÁY Chạy Bộ");
        mixedCaseEquipment.setType("Cardio");
        mixedCaseEquipment.setStatus("Available");

        equipmentService.createEquipment(mixedCaseEquipment);

        // When - Search with different cases
        List<EquipmentResponse> lowerCaseResults = equipmentService.getEquipmentsByName("máy chạy");
        List<EquipmentResponse> upperCaseResults = equipmentService.getEquipmentsByName("MÁY CHẠY");
        List<EquipmentResponse> mixedCaseResults = equipmentService.getEquipmentsByName("Máy Chạy");

        // Then - All should find the equipment
        assertEquals(1, lowerCaseResults.size());
        assertEquals(1, upperCaseResults.size());
        assertEquals(1, mixedCaseResults.size());

        assertEquals(lowerCaseResults.get(0).getId(), upperCaseResults.get(0).getId());
        assertEquals(lowerCaseResults.get(0).getId(), mixedCaseResults.get(0).getId());
    }

    @Test
    @DisplayName("Should handle equipment creation with all fields")
    void testCreateEquipment_WithAllFields() {
        // Given - Complete equipment data
        EquipmentDTO completeEquipmentDTO = new EquipmentDTO();
        completeEquipmentDTO.setName("Complete Test Equipment");
        completeEquipmentDTO.setType("Advanced Type");
        completeEquipmentDTO.setStatus("Available");
        completeEquipmentDTO.setLastMaintenance(Date.valueOf(LocalDate.now().minusDays(10)));

        // When
        EquipmentResponse createdEquipment = equipmentService.createEquipment(completeEquipmentDTO);

        // Then
        assertNotNull(createdEquipment);
        assertNotNull(createdEquipment.getId());
        assertEquals(completeEquipmentDTO.getName(), createdEquipment.getName());
        assertEquals(completeEquipmentDTO.getType(), createdEquipment.getType());
        assertEquals(completeEquipmentDTO.getStatus(), createdEquipment.getStatus());
        assertNotNull(createdEquipment.getLastMaintenance());

        // Verify in database
        Optional<EquipmentEntity> savedEntity = equipmentRepository.findById(createdEquipment.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals(completeEquipmentDTO.getName(), savedEntity.get().getName());
        assertEquals(completeEquipmentDTO.getType(), savedEntity.get().getType());
        assertEquals(completeEquipmentDTO.getStatus(), savedEntity.get().getStatus());
    }

    @Test
    @DisplayName("Should handle partial updates correctly")
    void testUpdateEquipment_PartialUpdate() {
        // Given - Create initial equipment
        EquipmentResponse createdEquipment = equipmentService.createEquipment(testEquipmentDTO);

        // Prepare partial update (only name and status)
        EquipmentDTO partialUpdateDTO = new EquipmentDTO();
        partialUpdateDTO.setName("Partially Updated Equipment");
        partialUpdateDTO.setStatus("Under Maintenance");
        // Leave type and lastMaintenance as null

        // When - Perform partial update
        EquipmentResponse updatedEquipment = equipmentService.updateEquipment(createdEquipment.getId(),
                partialUpdateDTO);

        // Then - Verify updated fields are changed, others remain
        assertNotNull(updatedEquipment);
        assertEquals(partialUpdateDTO.getName(), updatedEquipment.getName());
        assertEquals(partialUpdateDTO.getStatus(), updatedEquipment.getStatus());
        // Type should remain from original (due to merge logic)
        assertEquals(testEquipmentDTO.getType(), updatedEquipment.getType());
    }

    @Test
    @DisplayName("Should maintain data consistency across operations")
    void testDataConsistency_CreateUpdateDelete() {
        // Given - Create multiple equipments
        EquipmentDTO equipment1DTO = new EquipmentDTO();
        equipment1DTO.setName("Equipment 1");
        equipment1DTO.setType("Type 1");
        equipment1DTO.setStatus("Available");

        EquipmentDTO equipment2DTO = new EquipmentDTO();
        equipment2DTO.setName("Equipment 2");
        equipment2DTO.setType("Type 2");
        equipment2DTO.setStatus("Available");

        // When - Create both equipments
        EquipmentResponse created1 = equipmentService.createEquipment(equipment1DTO);
        EquipmentResponse created2 = equipmentService.createEquipment(equipment2DTO);

        // Then - Verify both exist
        List<EquipmentResponse> allEquipments = equipmentService.getAllEquipments();
        assertEquals(2, allEquipments.size());

        // When - Update first equipment
        EquipmentDTO updateDTO = new EquipmentDTO();
        updateDTO.setName("Updated Equipment 1");
        updateDTO.setStatus("Maintenance");

        EquipmentResponse updated1 = equipmentService.updateEquipment(created1.getId(), updateDTO);

        // Then - Verify update and second equipment unchanged
        assertEquals("Updated Equipment 1", updated1.getName());
        assertEquals("Maintenance", updated1.getStatus());

        EquipmentResponse unchanged2 = equipmentService.getEquipmentById(created2.getId());
        assertEquals("Equipment 2", unchanged2.getName());
        assertEquals("Available", unchanged2.getStatus());

        // When - Delete first equipment
        equipmentService.deleteEquipment(created1.getId());

        // Then - Verify only second equipment remains
        allEquipments = equipmentService.getAllEquipments();
        assertEquals(1, allEquipments.size());
        assertEquals(created2.getId(), allEquipments.get(0).getId());

        assertThrows(EntityNotFoundException.class, () ->
                equipmentService.getEquipmentById(created1.getId())
        );
    }
}
