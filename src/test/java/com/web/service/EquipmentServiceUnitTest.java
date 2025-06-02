package com.web.service;

import com.web.converter.EquipmentConverter;
import com.web.entity.EquipmentEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import com.web.repository.EquipmentRepository;
import com.web.service.impl.EquipmentServiceImpl;
import com.web.utils.CheckFieldObject;
import com.web.utils.MergeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Equipment Service Unit Tests")
class EquipmentServiceUnitTest {

    @Autowired
    private EquipmentServiceImpl equipmentService;

    @MockBean
    private EquipmentRepository equipmentRepository;

    @MockBean
    private EquipmentConverter equipmentConverter;

    @MockBean
    private MergeEntity<EquipmentEntity> mergeEntity;

    @MockBean
    private CheckFieldObject checkFieldObject;

    private EquipmentEntity testEquipmentEntity;
    private EquipmentDTO testEquipmentDTO;
    private EquipmentResponse testEquipmentResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        testEquipmentEntity = new EquipmentEntity();
        testEquipmentEntity.setId(1L);
        testEquipmentEntity.setName("Máy chạy bộ");
        testEquipmentEntity.setType("Cardio");
        testEquipmentEntity.setStatus("Available");
        testEquipmentEntity.setLastMaintenance(Date.valueOf(LocalDate.now().minusDays(30)));

        testEquipmentDTO = new EquipmentDTO();
        testEquipmentDTO.setId(1L);
        testEquipmentDTO.setName("Máy chạy bộ");
        testEquipmentDTO.setType("Cardio");
        testEquipmentDTO.setStatus("Available");
        testEquipmentDTO.setLastMaintenance(Date.valueOf(LocalDate.now().minusDays(30)));

        testEquipmentResponse = new EquipmentResponse();
        testEquipmentResponse.setId(1L);
        testEquipmentResponse.setName("Máy chạy bộ");
        testEquipmentResponse.setType("Cardio");
        testEquipmentResponse.setStatus("Available");
        testEquipmentResponse.setLastMaintenance("2025-05-03");
    }

    @Test
    @DisplayName("Should get equipment by ID successfully")
    void testGetEquipmentById_Success() {
        // Given
        Long equipmentId = 1L;
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(testEquipmentEntity));
        when(equipmentConverter.toResponse(testEquipmentEntity)).thenReturn(testEquipmentResponse);

        // When
        EquipmentResponse result = equipmentService.getEquipmentById(equipmentId);

        // Then
        assertNotNull(result);
        assertEquals(testEquipmentResponse.getId(), result.getId());
        assertEquals(testEquipmentResponse.getName(), result.getName());
        assertEquals(testEquipmentResponse.getType(), result.getType());
        assertEquals(testEquipmentResponse.getStatus(), result.getStatus());

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentConverter).toResponse(testEquipmentEntity);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when equipment not found by ID")
    void testGetEquipmentById_NotFound() {
        // Given
        Long equipmentId = 999L;
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.getEquipmentById(equipmentId);
        });

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentConverter, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should get all equipments successfully")
    void testGetAllEquipments_Success() {
        // Given
        EquipmentEntity equipment2 = new EquipmentEntity();
        equipment2.setId(2L);
        equipment2.setName("Tạ đơn");
        equipment2.setType("Weight");

        EquipmentResponse response2 = new EquipmentResponse();
        response2.setId(2L);
        response2.setName("Tạ đơn");
        response2.setType("Weight");

        List<EquipmentEntity> equipmentEntities = Arrays.asList(testEquipmentEntity, equipment2);
        List<EquipmentResponse> expectedResponses = Arrays.asList(testEquipmentResponse, response2);

        when(equipmentRepository.findAll()).thenReturn(equipmentEntities);
        when(equipmentConverter.toResponse(testEquipmentEntity)).thenReturn(testEquipmentResponse);
        when(equipmentConverter.toResponse(equipment2)).thenReturn(response2);

        // When
        List<EquipmentResponse> result = equipmentService.getAllEquipments();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedResponses.get(0).getId(), result.get(0).getId());
        assertEquals(expectedResponses.get(1).getId(), result.get(1).getId());

        verify(equipmentRepository).findAll();
        verify(equipmentConverter, times(2)).toResponse(any(EquipmentEntity.class));
    }

    @Test
    @DisplayName("Should return empty list when no equipments exist")
    void testGetAllEquipments_EmptyList() {
        // Given
        when(equipmentRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<EquipmentResponse> result = equipmentService.getAllEquipments();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(equipmentRepository).findAll();
        verify(equipmentConverter, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should get equipments by name successfully")
    void testGetEquipmentsByName_Success() {
        // Given
        String searchName = "máy";
        List<EquipmentEntity> equipmentEntities = Collections.singletonList(testEquipmentEntity);

        when(equipmentRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(equipmentEntities);
        when(equipmentConverter.toResponse(testEquipmentEntity)).thenReturn(testEquipmentResponse);

        // When
        List<EquipmentResponse> result = equipmentService.getEquipmentsByName(searchName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testEquipmentResponse.getName(), result.get(0).getName());

        verify(equipmentRepository).findByNameContainingIgnoreCase(searchName);
        verify(equipmentConverter).toResponse(testEquipmentEntity);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when no equipments found by name")
    void testGetEquipmentsByName_NotFound() {
        // Given
        String searchName = "nonexistent";
        when(equipmentRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(Collections.emptyList());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.getEquipmentsByName(searchName);
        });

        verify(equipmentRepository).findByNameContainingIgnoreCase(searchName);
        verify(equipmentConverter, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should create equipment successfully")
    void testCreateEquipment_Success() {
        // Given
        testEquipmentDTO.setId(null); // New equipment shouldn't have ID
        EquipmentEntity newEntity = new EquipmentEntity();
        newEntity.setName(testEquipmentDTO.getName());
        newEntity.setType(testEquipmentDTO.getType());
        newEntity.setStatus(testEquipmentDTO.getStatus());

        EquipmentEntity savedEntity = new EquipmentEntity();
        savedEntity.setId(1L);
        savedEntity.setName(testEquipmentDTO.getName());
        savedEntity.setType(testEquipmentDTO.getType());
        savedEntity.setStatus(testEquipmentDTO.getStatus());

        doNothing().when(checkFieldObject).check(EquipmentDTO.class, testEquipmentDTO, "name");
        when(equipmentConverter.toEntity(testEquipmentDTO)).thenReturn(newEntity);
        when(equipmentRepository.save(newEntity)).thenReturn(savedEntity);
        when(equipmentConverter.toResponse(savedEntity)).thenReturn(testEquipmentResponse);

        // When
        EquipmentResponse result = equipmentService.createEquipment(testEquipmentDTO);

        // Then
        assertNotNull(result);
        assertEquals(testEquipmentResponse.getId(), result.getId());
        assertEquals(testEquipmentResponse.getName(), result.getName());

        verify(checkFieldObject).check(EquipmentDTO.class, testEquipmentDTO, "name");
        verify(equipmentConverter).toEntity(testEquipmentDTO);
        verify(equipmentRepository).save(newEntity);
        verify(equipmentConverter).toResponse(savedEntity);
    }

    @Test
    @DisplayName("Should throw EntityAlreadyExistException when creating equipment with existing ID")
    void testCreateEquipment_AlreadyExists() {
        // Given
        testEquipmentDTO.setId(1L);
        doNothing().when(checkFieldObject).check(EquipmentDTO.class, testEquipmentDTO, "name");
        when(equipmentRepository.existsById(1L)).thenReturn(true);

        // When & Then
        assertThrows(EntityAlreadyExistException.class, () -> {
            equipmentService.createEquipment(testEquipmentDTO);
        });

        verify(checkFieldObject).check(EquipmentDTO.class, testEquipmentDTO, "name");
        verify(equipmentRepository).existsById(1L);
        verify(equipmentConverter, never()).toEntity(any());
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update equipment successfully")
    void testUpdateEquipment_Success() {
        // Given
        Long equipmentId = 1L;
        EquipmentEntity newEntity = new EquipmentEntity();
        newEntity.setName("Updated Equipment");
        newEntity.setType("Updated Type");

        EquipmentEntity mergedEntity = new EquipmentEntity();
        mergedEntity.setId(equipmentId);
        mergedEntity.setName("Updated Equipment");
        mergedEntity.setType("Updated Type");
        mergedEntity.setStatus(testEquipmentEntity.getStatus());

        EquipmentResponse updatedResponse = new EquipmentResponse();
        updatedResponse.setId(equipmentId);
        updatedResponse.setName("Updated Equipment");
        updatedResponse.setType("Updated Type");

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(testEquipmentEntity));
        when(equipmentConverter.toEntity(testEquipmentDTO)).thenReturn(newEntity);
        when(mergeEntity.merge(newEntity, testEquipmentEntity)).thenReturn(mergedEntity);
        when(equipmentRepository.save(mergedEntity)).thenReturn(mergedEntity);
        when(equipmentConverter.toResponse(mergedEntity)).thenReturn(updatedResponse);

        // When
        EquipmentResponse result = equipmentService.updateEquipment(equipmentId, testEquipmentDTO);

        // Then
        assertNotNull(result);
        assertEquals(updatedResponse.getId(), result.getId());
        assertEquals(updatedResponse.getName(), result.getName());

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentConverter).toEntity(testEquipmentDTO);
        verify(mergeEntity).merge(newEntity, testEquipmentEntity);
        verify(equipmentRepository).save(mergedEntity);
        verify(equipmentConverter).toResponse(mergedEntity);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating non-existent equipment")
    void testUpdateEquipment_NotFound() {
        // Given
        Long equipmentId = 999L;
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.updateEquipment(equipmentId, testEquipmentDTO);
        });

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentConverter, never()).toEntity(any());
        verify(mergeEntity, never()).merge(any(), any());
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete equipment successfully")
    void testDeleteEquipment_Success() {
        // Given
        Long equipmentId = 1L;
        when(equipmentRepository.existsById(equipmentId)).thenReturn(true);
        doNothing().when(equipmentRepository).deleteById(equipmentId);

        // When
        assertDoesNotThrow(() -> {
            equipmentService.deleteEquipment(equipmentId);
        });

        // Then
        verify(equipmentRepository).existsById(equipmentId);
        verify(equipmentRepository).deleteById(equipmentId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting non-existent equipment")
    void testDeleteEquipment_NotFound() {
        // Given
        Long equipmentId = 999L;
        when(equipmentRepository.existsById(equipmentId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            equipmentService.deleteEquipment(equipmentId);
        });

        verify(equipmentRepository).existsById(equipmentId);
        verify(equipmentRepository, never()).deleteById(any());
    }
}
