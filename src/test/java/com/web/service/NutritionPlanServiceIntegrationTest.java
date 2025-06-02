package com.web.service;

import com.web.entity.NutritionPlanEntity;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;
import com.web.repository.NutritionPlanRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NutritionPlanServiceIntegrationTest {

    @Autowired
    private NutritionPlanService nutritionPlanService;

    @Autowired
    private NutritionPlanRepository nutritionPlanRepository;

    @Test
    void testCreateNutritionPlan_Success() {
        // Given
        NutritionPlanDTO nutritionPlanDTO = new NutritionPlanDTO();
        nutritionPlanDTO.setMemberEntityId(1L);
        nutritionPlanDTO.setPlanDate(Date.valueOf("2025-06-01"));
        nutritionPlanDTO.setMealDescription("Integration Test: Sáng: cháo, Trưa: gà, Tối: cá");
        nutritionPlanDTO.setCalories("2100");

        // When
        NutritionPlanResponse result = nutritionPlanService.createNutritionPlan(nutritionPlanDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getMemberEntityId());
        assertEquals("2025-06-01", result.getPlanDate());
        assertEquals("Integration Test: Sáng: cháo, Trưa: gà, Tối: cá", result.getMealDescription());
        assertEquals("2100", result.getCalories());

        // Verify in database
        Optional<NutritionPlanEntity> savedEntity = nutritionPlanRepository.findById(result.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals("Integration Test: Sáng: cháo, Trưa: gà, Tối: cá", savedEntity.get().getMealDescription());
    }

    @Test
    void testCreateNutritionPlan_WithVietnameseContent() {
        // Given
        NutritionPlanDTO nutritionPlanDTO = new NutritionPlanDTO();
        nutritionPlanDTO.setMemberEntityId(2L);
        nutritionPlanDTO.setPlanDate(Date.valueOf("2025-06-02"));
        nutritionPlanDTO.setMealDescription("Sáng: phở bò, Trưa: cơm tấm, Tối: bún bò Huế");
        nutritionPlanDTO.setCalories("2200");

        // When
        NutritionPlanResponse result = nutritionPlanService.createNutritionPlan(nutritionPlanDTO);

        // Then
        assertNotNull(result);
        assertEquals("Sáng: phở bò, Trưa: cơm tấm, Tối: bún bò Huế", result.getMealDescription());

        // Verify Vietnamese content is preserved in database
        Optional<NutritionPlanEntity> entity = nutritionPlanRepository.findById(result.getId());
        assertTrue(entity.isPresent());
        assertEquals("Sáng: phở bò, Trưa: cơm tấm, Tối: bún bò Huế", entity.get().getMealDescription());
    }

    @Test
    void testGetNutritionPlanById_WithExistingData() {
        // Given - Using data from init.sql (plan_id=1, member_id=1)
        Long existingPlanId = 1L;

        // When
        NutritionPlanResponse result = nutritionPlanService.getNutritionPlanById(existingPlanId);

        // Then
        assertNotNull(result);
        assertEquals(existingPlanId, result.getId());
        assertEquals(1L, result.getMemberEntityId());
        assertEquals("2025-05-28", result.getPlanDate());
        assertEquals("Sáng: yến mạch, Trưa: ức gà, Tối: cá hồi", result.getMealDescription());
        assertEquals("2000", result.getCalories());
    }

    @Test
    void testGetNutritionPlanById_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> nutritionPlanService.getNutritionPlanById(nonExistentId));
    }

    @Test
    void testGetAllByMemberId_WithExistingData() {
        // Given - Member 1 has data in init.sql
        Long memberId = 1L;

        // When
        List<NutritionPlanResponse> result = nutritionPlanService.getAllByMemberId(memberId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Verify at least one plan exists for member 1
        assertTrue(result.stream().anyMatch(plan -> plan.getMemberEntityId().equals(memberId) &&
                "Sáng: yến mạch, Trưa: ức gà, Tối: cá hồi".equals(plan.getMealDescription())));
    }

    @Test
    void testGetAllByMemberId_NoPlans_ReturnsEmptyList() {
        // Given - Member 99 doesn't exist in test data
        Long memberId = 99L;

        // When
        List<NutritionPlanResponse> result = nutritionPlanService.getAllByMemberId(memberId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllByMemberIdAndDate_Success() {
        // Given - Data from init.sql: member 1 has plan on 2025-05-28
        Long memberId = 1L;
        Date planDate = Date.valueOf("2025-05-28");

        // When
        List<NutritionPlanResponse> result = nutritionPlanService.getAllByMemberIdAndDate(memberId, planDate);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Verify plan exists for specific date
        assertTrue(result.stream().anyMatch(plan -> plan.getMemberEntityId().equals(memberId) &&
                plan.getPlanDate().equals("2025-05-28") &&
                "Sáng: yến mạch, Trưa: ức gà, Tối: cá hồi".equals(plan.getMealDescription())));
    }

    @Test
    void testGetAllByMemberIdAndDate_NoPlansForDate() {
        // Given
        Long memberId = 1L;
        Date planDate = Date.valueOf("2025-12-31"); // No plans for this date

        // When
        List<NutritionPlanResponse> result = nutritionPlanService.getAllByMemberIdAndDate(memberId, planDate);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateNutritionPlan_Success() {
        // Given - Use existing plan from init.sql
        Long planId = 1L;
        NutritionPlanDTO updateDTO = new NutritionPlanDTO();
        updateDTO.setMemberEntityId(1L);
        updateDTO.setPlanDate(Date.valueOf("2025-05-28"));
        updateDTO.setMealDescription("Updated: Sáng: bánh mì, Trưa: phở, Tối: cơm chiên");
        updateDTO.setCalories("2300");

        // When
        NutritionPlanResponse result = nutritionPlanService.updateNutritionPlan(planId, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals(planId, result.getId());
        assertEquals("Updated: Sáng: bánh mì, Trưa: phở, Tối: cơm chiên", result.getMealDescription());
        assertEquals("2300", result.getCalories());

        // Verify in database
        Optional<NutritionPlanEntity> updatedEntity = nutritionPlanRepository.findById(planId);
        assertTrue(updatedEntity.isPresent());
        assertEquals("Updated: Sáng: bánh mì, Trưa: phở, Tối: cơm chiên", updatedEntity.get().getMealDescription());
        assertEquals("2300", updatedEntity.get().getCalories());
    }

    @Test
    void testUpdateNutritionPlan_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long nonExistentId = 999L;
        NutritionPlanDTO updateDTO = new NutritionPlanDTO();
        updateDTO.setMealDescription("This should fail");

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> nutritionPlanService.updateNutritionPlan(nonExistentId, updateDTO));
    }

    @Test
    void testDeleteNutritionPlan_Success() {
        // Given - Create a plan to delete
        NutritionPlanDTO nutritionPlanDTO = new NutritionPlanDTO();
        nutritionPlanDTO.setMemberEntityId(1L);
        nutritionPlanDTO.setPlanDate(Date.valueOf("2025-06-10"));
        nutritionPlanDTO.setMealDescription("Plan to be deleted");
        nutritionPlanDTO.setCalories("1500");

        NutritionPlanResponse created = nutritionPlanService.createNutritionPlan(nutritionPlanDTO);
        Long planId = created.getId();

        // Verify plan exists
        assertTrue(nutritionPlanRepository.existsById(planId));

        // When
        nutritionPlanService.deleteNutritionPlan(planId);

        // Then
        assertFalse(nutritionPlanRepository.existsById(planId));
    }

    @Test
    void testDeleteNutritionPlan_NotFound_ThrowsEntityNotFoundException() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> nutritionPlanService.deleteNutritionPlan(nonExistentId));
    }

    @Test
    void testNutritionPlan_DataConsistency() {
        // Given - Create a plan with Vietnamese content
        NutritionPlanDTO nutritionPlanDTO = new NutritionPlanDTO();
        nutritionPlanDTO.setMemberEntityId(3L);
        nutritionPlanDTO.setPlanDate(Date.valueOf("2025-06-15"));
        nutritionPlanDTO.setMealDescription("Consistency Test: Sáng: bánh cuốn, Trưa: bún chả, Tối: chả cá");
        nutritionPlanDTO.setCalories("1950");

        // When - Create and retrieve
        NutritionPlanResponse created = nutritionPlanService.createNutritionPlan(nutritionPlanDTO);
        NutritionPlanResponse retrieved = nutritionPlanService.getNutritionPlanById(created.getId());

        // Then - Verify consistency
        assertEquals(created.getId(), retrieved.getId());
        assertEquals(created.getMemberEntityId(), retrieved.getMemberEntityId());
        assertEquals(created.getPlanDate(), retrieved.getPlanDate());
        assertEquals(created.getMealDescription(), retrieved.getMealDescription());
        assertEquals(created.getCalories(), retrieved.getCalories());

        // Verify in member's plan list
        List<NutritionPlanResponse> memberPlans = nutritionPlanService.getAllByMemberId(3L);
        assertTrue(memberPlans.stream().anyMatch(plan -> plan.getId().equals(created.getId()) &&
                "Consistency Test: Sáng: bánh cuốn, Trưa: bún chả, Tối: chả cá".equals(plan.getMealDescription())));

        // Update and verify
        NutritionPlanDTO updateDTO = new NutritionPlanDTO();
        updateDTO.setMemberEntityId(3L);
        updateDTO.setPlanDate(Date.valueOf("2025-06-15"));
        updateDTO.setMealDescription("Updated: Sáng: phở, Trưa: bún bò, Tối: cơm tấm");
        updateDTO.setCalories("2000");

        NutritionPlanResponse updated = nutritionPlanService.updateNutritionPlan(created.getId(), updateDTO);
        assertEquals("Updated: Sáng: phở, Trưa: bún bò, Tối: cơm tấm", updated.getMealDescription());
        assertEquals("2000", updated.getCalories());

        // Delete and verify
        nutritionPlanService.deleteNutritionPlan(created.getId());
        assertThrows(EntityNotFoundException.class, () -> nutritionPlanService.getNutritionPlanById(created.getId()));
    }
}
