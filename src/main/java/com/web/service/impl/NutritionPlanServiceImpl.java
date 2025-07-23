package com.web.service.impl;

import com.web.converter.NutritionPlanConverter;
import com.web.converter.NutritionPlanDetailConverter;
import com.web.entity.MemberEntity;
import com.web.entity.NutritionPlanDetailEntity;
import com.web.entity.NutritionPlanEntity;
import com.web.entity.TrainerEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.NutritionPlanDTO;
import com.web.model.response.*;
import com.web.repository.MemberRepository;
import com.web.repository.NutritionPlanDetailRepository;
import com.web.repository.NutritionPlanRepository;
import com.web.repository.TrainerRepository;
import com.web.service.NutritionPlanService;
import com.web.utils.CalculatorTimestamp;
import com.web.utils.CheckFieldObject;
import com.web.utils.MergeObjectUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NutritionPlanServiceImpl implements NutritionPlanService {

    private TrainerRepository trainerRepository;

    private NutritionPlanConverter nutritionPlanConverter;

    private MemberRepository memberRepository;

    private NutritionPlanRepository nutritionPlanRepository;

    private NutritionPlanDetailRepository nutritionPlanDetailRepository;

    private NutritionPlanDetailConverter nutritionPlanDetailConverter;

    @Autowired
    public void setNutritionPlanDetailConverter(NutritionPlanDetailConverter nutritionPlanDetailConverter) {
        this.nutritionPlanDetailConverter = nutritionPlanDetailConverter;
    }

    @Autowired
    public void setNutritionPlanDetailRepository(NutritionPlanDetailRepository nutritionPlanDetailRepository) {
        this.nutritionPlanDetailRepository = nutritionPlanDetailRepository;
    }

    @Autowired
    public void setNutritionPlanRepository(NutritionPlanRepository nutritionPlanRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setNutritionPlanConverter(NutritionPlanConverter nutritionPlanConverter) {
        this.nutritionPlanConverter = nutritionPlanConverter;
    }

    @Override
    public List<MemberOfTrainerResponse> getMemberOfTrainer(Long id) {
        TrainerEntity trainerEntity = trainerRepository.findById(id).get();

        List<MemberEntity> memberEntities = trainerEntity.getMemberEntities();
        List<MemberOfTrainerResponse> memberOfTrainerResponses = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntities) {
            MemberOfTrainerResponse memberOfTrainerResponse = new MemberOfTrainerResponse();
            memberOfTrainerResponse.setId(String.valueOf(memberEntity.getId()));
            memberOfTrainerResponse.setName(memberEntity.getUserEntity().getFullName());
            memberOfTrainerResponses.add(memberOfTrainerResponse);
        }
        return memberOfTrainerResponses;
    }

    @Override
    public boolean saveNutritionPlan(NutritionPlanDTO dto) {
        NutritionPlanEntity nutritionPlanEntity = nutritionPlanConverter.toEntity(dto);

        MemberEntity memberEntity = memberRepository.findById(dto.getMemberId()).get();

        nutritionPlanEntity.setMemberEntity(memberEntity);

        return nutritionPlanRepository.save(nutritionPlanEntity) != null;
    }

    @Override
    public List<NutritionPlanResponsePlus> getNutritionPlanByTrainerId(Long id) {
        List<NutritionPlanEntity> nutritionPlanEntities = nutritionPlanRepository.findByMemberEntity_TrainerEntity_Id(id);

        return nutritionPlanEntities.stream().map(nutritionPlanConverter::toResponsePlus).collect(Collectors.toList());
    }

    @Override
    public NutritionPlanResponsePlus getNutritionPlanById(Long id) {
        List<NutritionPlanEntity> nutritionPlanEntities = nutritionPlanRepository.findByMemberEntity_TrainerEntity_Id(id);

        return nutritionPlanConverter.toResponsePlus(nutritionPlanEntities.get(0));
    }

    @Override
    public List<NutritionPlanDetailResponse> getNutritionPlanDetailByPlanId(Long id) {
        NutritionPlanEntity nutritionPlanEntity = nutritionPlanRepository.findById(id).get();

        List<NutritionPlanDetailEntity> nutritionPlanDetailEntities = nutritionPlanEntity.getNutritionPlanDetailEntityList();

        return nutritionPlanDetailEntities.stream().map(nutritionPlanDetailConverter::toResponse).collect(Collectors.toList());
    }

}
