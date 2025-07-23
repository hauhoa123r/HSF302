package com.web.service.impl;

import com.web.converter.TrainerConverter;
import com.web.entity.TrainerEntity;
import com.web.enums.operation.ComparisonOperator;
import com.web.exception.ErrorResponse;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.TrainerDTO;
import com.web.model.dto.UserDTO;
import com.web.model.response.TrainerResponse;
import com.web.repository.TrainerRepository;
import com.web.service.TrainerService;
import com.web.utils.MergeObjectUtils;
import com.web.utils.PageUtils;
import com.web.utils.specification.SpecificationUtils;
import com.web.utils.specification.search.SearchCriteria;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private TrainerRepository trainerRepository;
    private TrainerConverter trainerConverter;
    private SpecificationUtils<TrainerEntity> specificationUtils;
    private PageUtils<TrainerEntity> pageUtils;
    private MergeObjectUtils mergeObjectUtils;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setSpecificationUtils(SpecificationUtils<TrainerEntity> specificationUtils) {
        this.specificationUtils = specificationUtils;
    }

    @Autowired
    public void setTrainerConverter(TrainerConverter trainerConverter) {
        this.trainerConverter = trainerConverter;
    }

    @Autowired
    public void setPageUtils(PageUtils<TrainerEntity> pageUtils) {
        this.pageUtils = pageUtils;
    }

    @Autowired
    public void setMergeObjectUtils(MergeObjectUtils mergeObjectUtils) {
        this.mergeObjectUtils = mergeObjectUtils;
    }

    @Override
    public Long countTrainers() {
        return trainerRepository.count();
    }

    @Override
    public TrainerResponse getTrainerById(Long id) {
        TrainerEntity trainerEntity = trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TrainerEntity.class, id));
        return trainerConverter.toResponse(trainerEntity);
    }

    @Override
    public List<TrainerResponse> getTrainers() {
        return trainerRepository.findAllByStatus("ACTIVE")
                .stream()
                .map(trainerConverter::toResponse)
                .toList();
    }

    @Override
    public Page<TrainerResponse> getTrainers(int page, int size, TrainerDTO trainerDTO) {
        Sort sort = Sort.unsorted();
        if (trainerDTO.getSortFieldName() != null && trainerDTO.getSortDirection() != null) {
            sort = Sort.by(Sort.Direction.fromString(trainerDTO.getSortDirection()), trainerDTO.getSortFieldName());
        }
        Pageable pageable = pageUtils.getPageable(page, size, sort);
        List<SearchCriteria> searchCriteria = List.of(
                new SearchCriteria(
                        "userEntity.fullName",
                        ComparisonOperator.CONTAINS,
                        Optional.ofNullable(trainerDTO.getUserEntity())
                                .map(UserDTO::getFullName)
                                .orElse(null),
                        null
                ),
                new SearchCriteria(
                        "specialization",
                        ComparisonOperator.EQUALS,
                        Optional.ofNullable(trainerDTO.getSpecialization()).orElse(null),
                        null
                ),
                new SearchCriteria(
                        "status",
                        ComparisonOperator.EQUALS,
                        Optional.ofNullable(trainerDTO.getStatus()).orElse(null),
                        null
                ),
                new SearchCriteria(
                        "userEntity.email",
                        ComparisonOperator.CONTAINS,
                        Optional.ofNullable(trainerDTO.getUserEntity())
                                .map(UserDTO::getEmail)
                                .orElse(null),
                        null
                ),
                new SearchCriteria(
                        "userEntity.phone",
                        ComparisonOperator.CONTAINS,
                        Optional.ofNullable(trainerDTO.getUserEntity())
                                .map(UserDTO::getPhone)
                                .orElse(null),
                        null
                )
        );
        Page<TrainerEntity> trainerEntityPage = trainerRepository.findAll(
                specificationUtils.getSearchSpecifications(searchCriteria),
                pageable
        );
        pageUtils.validatePage(trainerEntityPage, TrainerEntity.class);
        return trainerEntityPage.map(trainerConverter::toResponse);
    }

    @Override
    public List<String> getAllSpecializations() {
        return trainerRepository.findAllSpecializations();
    }

    @Override
    public void addTrainer(TrainerDTO trainerDTO) {
        trainerDTO.setId(null);
        // Kiem tra trung email trong db
        if (trainerRepository.existsByUserEntityEmail(trainerDTO.getUserEntity().getEmail())) {
            throw new ErrorResponse("Email đã tồn tại trong hệ thống", 400);
        }
        // Kiem tra trung phone trong db
        if (trainerRepository.existsByUserEntityPhone(trainerDTO.getUserEntity().getPhone())) {
            throw new ErrorResponse("Số điện thoại đã tồn tại trong hệ thống", 400);
        }
        // Kiem tra trung idCard trong db
        if (trainerRepository.existsByUserEntityIdCard(trainerDTO.getUserEntity().getIdCard())) {
            throw new ErrorResponse("Số CMND/CCCD đã tồn tại trong hệ thống", 400);
        }
        TrainerEntity trainerEntity = trainerConverter.toEntity(trainerDTO);
        trainerRepository.save(trainerEntity);
    }

    @Override
    public void updateTrainer(Long id, TrainerDTO trainerDTO) {
        trainerDTO.setId(id);
        TrainerEntity target = trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TrainerEntity.class, id));
        TrainerEntity source = trainerConverter.toEntity(trainerDTO);
        mergeObjectUtils.mergeNonNullFields(source, target);
        trainerRepository.save(target);
    }

    @Override
    public void deleteTrainer(Long id) {
        TrainerEntity trainerEntity = trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TrainerEntity.class, id));
        trainerRepository.delete(trainerEntity);
    }
}
