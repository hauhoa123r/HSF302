package com.web.service.impl;

import com.web.converter.NutritionPlanConverter;
import com.web.entity.NutritionPlanEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.NutritionPlanResponse;
import com.web.repository.NutritionPlanRepository;
import com.web.service.NutritionPlanService;
import com.web.utils.CalculatorTimestamp;
import com.web.utils.CheckFieldObject;
import com.web.utils.MergeEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@Transactional
public class NutritionPlanServiceImpl implements NutritionPlanService {

    private NutritionPlanRepository nutritionPlanRepository;
    private NutritionPlanConverter nutritionPlanConverter;
    private CheckFieldObject checkFieldObject;
    private MergeEntity<NutritionPlanEntity> mergeEntity;

    @Autowired
    public void setNutritionPlanRepository(NutritionPlanRepository nutritionPlanRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
    }

    @Autowired
    public void setNutritionPlanConverter(NutritionPlanConverter nutritionPlanConverter) {
        this.nutritionPlanConverter = nutritionPlanConverter;
    }

    @Autowired
    public void setCheckFieldObject(CheckFieldObject checkFieldObject) {
        this.checkFieldObject = checkFieldObject;
    }

    @Autowired
    public void setMergeEntity(MergeEntity<NutritionPlanEntity> mergeEntity) {
        this.mergeEntity = mergeEntity;
    }

    @Override
    public NutritionPlanResponse createNutritionPlan(NutritionPlanDTO nutritionPlanDTO) {
        checkFieldObject.check(NutritionPlanDTO.class, nutritionPlanDTO);

        if (nutritionPlanDTO.getId() != null
                && nutritionPlanRepository.existsById(nutritionPlanDTO.getId())) {
            throw new EntityAlreadyExistException(NutritionPlanEntity.class);
        }

        NutritionPlanEntity nutritionPlanEntity = nutritionPlanConverter.toEntity(nutritionPlanDTO);

        nutritionPlanEntity = nutritionPlanRepository.save(nutritionPlanEntity);

        return nutritionPlanConverter.toResponse(nutritionPlanEntity);
    }

    @Override
    public NutritionPlanResponse getNutritionPlanById(Long id) {
        return nutritionPlanRepository.findById(id)
                .map(nutritionPlanConverter::toResponse)
                .orElseThrow(() -> new EntityNotFoundException(NutritionPlanEntity.class));
    }

    @Override
    public List<NutritionPlanResponse> getAllByMemberId(Long memberId) {
        List<NutritionPlanEntity> nutritionPlanEntities = nutritionPlanRepository.findAllByMemberEntityId(memberId);

        return nutritionPlanEntities.stream().map(nutritionPlanConverter::toResponse).toList();
    }

    @Override
    public List<NutritionPlanResponse> getAllByMemberIdAndDate(Long memberId, Date date) {
        List<NutritionPlanEntity> nutritionPlanEntities = nutritionPlanRepository.findAllByMemberEntityIdAndPlanDate(
                memberId,
                date);

        return nutritionPlanEntities.stream()
                .map(nutritionPlanConverter::toResponse)
                .toList();
    }

    @Override
    public List<NutritionPlanResponse> getAllByMemberIdAndWeek(Long memberId, Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        List<NutritionPlanEntity> nutritionPlanEntities = nutritionPlanRepository
                .findAllByMemberEntityIdAndPlanDateBetween(
                        memberId,
                        new Date(calculatorTimestamp.getStartOfWeek().getTime()),
                        new Date(calculatorTimestamp.getEndOfWeek().getTime()));

        return nutritionPlanEntities.stream()
                .map(nutritionPlanConverter::toResponse)
                .toList();
    }

    @Override
    public List<NutritionPlanResponse> getAllByMemberIdAndMonth(Long memberId, Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        List<NutritionPlanEntity> nutritionPlanEntities = nutritionPlanRepository
                .findAllByMemberEntityIdAndPlanDateBetween(
                        memberId,
                        new Date(calculatorTimestamp.getStartOfMonth().getTime()),
                        new Date(calculatorTimestamp.getEndOfMonth().getTime()));

        return nutritionPlanEntities.stream()
                .map(nutritionPlanConverter::toResponse)
                .toList();
    }

    @Override
    public NutritionPlanResponse updateNutritionPlan(Long id, NutritionPlanDTO nutritionPlanDTO) {
        NutritionPlanEntity oldNutritionPlanEntity = nutritionPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NutritionPlanEntity.class));

        NutritionPlanEntity newNutritionPlanEntity = nutritionPlanConverter.toEntity(nutritionPlanDTO);

        NutritionPlanEntity mergedNutritionPlanEntity = mergeEntity.merge(newNutritionPlanEntity, oldNutritionPlanEntity);

        return nutritionPlanConverter.toResponse(nutritionPlanRepository.save(mergedNutritionPlanEntity));
    }

    @Override
    public void deleteNutritionPlan(Long id) {
        if (!nutritionPlanRepository.existsById(id)) {
            throw new EntityNotFoundException(NutritionPlanEntity.class);
        }

        nutritionPlanRepository.deleteById(id);
    }
}
