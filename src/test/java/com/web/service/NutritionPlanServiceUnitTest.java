package com.web.service;

import com.web.converter.NutritionPlanConverter;
import com.web.entity.MemberEntity;
import com.web.entity.NutritionPlanEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;
import com.web.repository.NutritionPlanRepository;
import com.web.service.impl.NutritionPlanServiceImpl;
import com.web.utils.CheckFieldObject;
import com.web.utils.MergeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class NutritionPlanServiceUnitTest {

    @MockBean
    private NutritionPlanRepository nutritionPlanRepository;

    @MockBean
    private NutritionPlanConverter nutritionPlanConverter;

    @MockBean
    private CheckFieldObject checkFieldObject;

    @MockBean
    private MergeEntity<NutritionPlanEntity> mergeEntity;

    private NutritionPlanService nutritionPlanService;

    private NutritionPlanDTO nutritionPlanDTO;
    private NutritionPlanEntity nutritionPlanEntity;
    private NutritionPlanResponse nutritionPlanResponse;
    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        nutritionPlanService = new NutritionPlanServiceImpl();
        ((NutritionPlanServiceImpl) nutritionPlanService).setNutritionPlanRepository(nutritionPlanRepository);
        ((NutritionPlanServiceImpl) nutritionPlanService).setNutritionPlanConverter(nutritionPlanConverter);
        ((NutritionPlanServiceImpl) nutritionPlanService).setCheckFieldObject(checkFieldObject);
        ((NutritionPlanServiceImpl) nutritionPlanService).setMergeEntity(mergeEntity);

        // Setup test data
        memberEntity = new MemberEntity();
        memberEntity.setId(1L);

        nutritionPlanDTO = new NutritionPlanDTO();
        nutritionPlanDTO.setId(null);
        nutritionPlanDTO.setMemberEntityId(1L);
        nutritionPlanDTO.setPlanDate(Date.valueOf("2025-05-28"));
        nutritionPlanDTO.setMealDescription("Sáng: yến mạch, Trưa: ức gà, Tối: cá hồi");
        nutritionPlanDTO.setCalories("2000");

        nutritionPlanEntity = new NutritionPlanEntity();
        nutritionPlanEntity.setId(1L);
        nutritionPlanEntity.setMemberEntity(memberEntity);
        nutritionPlanEntity.setPlanDate(Date.valueOf("2025-05-28"));
        nutritionPlanEntity.setMealDescription("Sáng: yến mạch, Trưa: ức gà, Tối: cá hồi");
        nutritionPlanEntity.setCalories("2000");

        nutritionPlanResponse = new NutritionPlanResponse();
        nutritionPlanResponse.setId(1L);
        nutritionPlanResponse.setMemberEntityId(1L);
        nutritionPlanResponse.setPlanDate("2025-05-28");
        nutritionPlanResponse.setMealDescription("Sáng: yến mạch, Trưa: ức gà, Tối: cá hồi");
        nutritionPlanResponse.setCalories("2000");
    }

    @Test
    void testCreateNutritionPlan_Success() {
        // Given
        when(nutritionPlanConverter.toEntity(nutritionPlanDTO)).thenReturn(nutritionPlanEntity);
        when(nutritionPlanRepository.save(nutritionPlanEntity)).thenReturn(nutritionPlanEntity);
        when(nutritionPlanConverter.toResponse(nutritionPlanEntity)).thenReturn(nutritionPlanResponse);

        // When
        NutritionPlanResponse result = nutritionPlanService.createNutritionPlan(nutritionPlanDTO);

        // Then
        assertNotNull(result);
        assertEquals(nutritionPlanResponse.getId(), result.getId());
        assertEquals(nutritionPlanResponse.getMemberEntityId(), result.getMemberEntityId());
        assertEquals(nutritionPlanResponse.getMealDescription(), result.getMealDescription());

        verify(checkFieldObject).check(NutritionPlanDTO.class, nutritionPlanDTO);
        verify(nutritionPlanConverter).toEntity(nutritionPlanDTO);
        verify(nutritionPlanRepository).save(nutritionPlanEntity);
        verify(nutritionPlanConverter).toResponse(nutritionPlanEntity);
    }

    @Test
    void testCreateNutritionPlan_WithExistingId_ThrowsEntityAlreadyExistException() {
        // Given
        nutritionPlanDTO.setId(1L);
        when(nutritionPlanRepository.existsById(1L)).thenReturn(true);

        // When & Then
        assertThrows(EntityAlreadyExistException.class,
                () -> nutritionPlanService.createNutritionPlan(nutritionPlanDTO));

        verify(checkFieldObject).check(NutritionPlanDTO.class, nutritionPlanDTO);
        verify(nutritionPlanRepository).existsById(1L);
        verify(nutritionPlanConverter, never()).toEntity(any());
        verify(nutritionPlanRepository, never()).save(any());
    }

    @Test
    void testGetNutritionPlanById_Success() {
        // Given
        Long planId = 1L;
        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.of(nutritionPlanEntity));
        when(nutritionPlanConverter.toResponse(nutritionPlanEntity)).thenReturn(nutritionPlanResponse);

        // When
        NutritionPlanResponse result = nutritionPlanService.getNutritionPlanById(planId);

        // Then
        assertNotNull(result);
        assertEquals(nutritionPlanResponse.getId(), result.getId());
        assertEquals(nutritionPlanResponse.getMealDescription(), result.getMealDescription());

        verify(nutritionPlanRepository).findById(planId);
        verify(nutritionPlanConverter).toResponse(nutritionPlanEntity);
    }

    @Test
    void testGetNutritionPlanById_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long planId = 1L;
        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> nutritionPlanService.getNutritionPlanById(planId));

        verify(nutritionPlanRepository).findById(planId);
        verify(nutritionPlanConverter, never()).toResponse(any());
    }

    @Test
    void testGetAllByMemberId_WithPlans_ReturnsList() {
        // Given
        Long memberId = 1L;
        List<NutritionPlanEntity> entities = Collections.singletonList(nutritionPlanEntity);

        when(nutritionPlanRepository.findAllByMemberEntityId(memberId)).thenReturn(entities);
        when(nutritionPlanConverter.toResponse(nutritionPlanEntity)).thenReturn(nutritionPlanResponse);

        // When
        List<NutritionPlanResponse> result = nutritionPlanService.getAllByMemberId(memberId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(nutritionPlanResponse.getId(), result.get(0).getId());

        verify(nutritionPlanRepository).findAllByMemberEntityId(memberId);
        verify(nutritionPlanConverter).toResponse(nutritionPlanEntity);
    }

    @Test
    void testGetAllByMemberId_WithNoPlans_ReturnsEmptyList() {
        // Given
        Long memberId = 1L;
        when(nutritionPlanRepository.findAllByMemberEntityId(memberId)).thenReturn(Collections.emptyList());

        // When
        List<NutritionPlanResponse> result = nutritionPlanService.getAllByMemberId(memberId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(nutritionPlanRepository).findAllByMemberEntityId(memberId);
        verify(nutritionPlanConverter, never()).toResponse(any());
    }

    @Test
    void testGetAllByMemberIdAndDate_Success() {
        // Given
        Long memberId = 1L;
        Date planDate = Date.valueOf("2025-05-28");
        List<NutritionPlanEntity> entities = Collections.singletonList(nutritionPlanEntity);

        when(nutritionPlanRepository.findAllByMemberEntityIdAndPlanDate(memberId, planDate)).thenReturn(entities);
        when(nutritionPlanConverter.toResponse(nutritionPlanEntity)).thenReturn(nutritionPlanResponse);

        // When
        List<NutritionPlanResponse> result = nutritionPlanService.getAllByMemberIdAndDate(memberId, planDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(nutritionPlanResponse.getId(), result.get(0).getId());

        verify(nutritionPlanRepository).findAllByMemberEntityIdAndPlanDate(memberId, planDate);
        verify(nutritionPlanConverter).toResponse(nutritionPlanEntity);
    }

    @Test
    void testUpdateNutritionPlan_Success() {
        // Given
        Long planId = 1L;

        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.of(nutritionPlanEntity));
        when(nutritionPlanConverter.toEntity(nutritionPlanDTO)).thenReturn(nutritionPlanEntity);
        when(mergeEntity.merge(any(NutritionPlanEntity.class), any(NutritionPlanEntity.class)))
                .thenReturn(nutritionPlanEntity);
        when(nutritionPlanRepository.save(nutritionPlanEntity)).thenReturn(nutritionPlanEntity);
        when(nutritionPlanConverter.toResponse(nutritionPlanEntity)).thenReturn(nutritionPlanResponse);

        // When
        NutritionPlanResponse result = nutritionPlanService.updateNutritionPlan(planId, nutritionPlanDTO);

        // Then
        assertNotNull(result);
        assertEquals(nutritionPlanResponse.getId(), result.getId());

        verify(nutritionPlanRepository).findById(planId);
        verify(nutritionPlanRepository).save(nutritionPlanEntity);
        verify(nutritionPlanConverter).toResponse(nutritionPlanEntity);
    }

    @Test
    void testUpdateNutritionPlan_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long planId = 1L;
        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> nutritionPlanService.updateNutritionPlan(planId, nutritionPlanDTO));

        verify(nutritionPlanRepository).findById(planId);
        verify(nutritionPlanRepository, never()).save(any());
    }

    @Test
    void testDeleteNutritionPlan_Success() {
        // Given
        Long planId = 1L;
        when(nutritionPlanRepository.existsById(planId)).thenReturn(true);

        // When
        nutritionPlanService.deleteNutritionPlan(planId);

        // Then
        verify(nutritionPlanRepository).existsById(planId);
        verify(nutritionPlanRepository).deleteById(planId);
    }

    @Test
    void testDeleteNutritionPlan_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long planId = 1L;
        when(nutritionPlanRepository.existsById(planId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> nutritionPlanService.deleteNutritionPlan(planId));

        verify(nutritionPlanRepository).existsById(planId);
        verify(nutritionPlanRepository, never()).deleteById(any());
    }
}
