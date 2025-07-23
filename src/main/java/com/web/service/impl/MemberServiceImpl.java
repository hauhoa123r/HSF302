package com.web.service.impl;

import com.web.converter.MemberConverter;
import com.web.converter.UserConverter;
import com.web.entity.MemberEntity;
import com.web.entity.MemberPackageEntity;
import com.web.entity.UserEntity;
import com.web.enums.operation.AggregationFunction;
import com.web.enums.operation.ComparisonOperator;
import com.web.exception.ErrorResponse;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.MemberDTO;
import com.web.model.dto.UserDTO;
import com.web.model.response.MemberResponse;
import com.web.repository.MemberRepository;
import com.web.service.MemberService;
import com.web.utils.FieldNameUtils;
import com.web.utils.MergeObjectUtils;
import com.web.utils.PageUtils;
import com.web.utils.specification.PageSpecificationUtils;
import com.web.utils.specification.SpecificationUtils;
import com.web.utils.specification.search.SearchCriteria;
import com.web.utils.specification.sort.SortCriteria;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;
    private MemberConverter memberConverter;
    private UserConverter userConverter;
    private SpecificationUtils<MemberEntity> specificationUtils;
    private PageSpecificationUtils<MemberEntity> pageSpecificationUtils;
    private PageUtils<MemberEntity> pageUtils;
    private MergeObjectUtils mergeObjectUtils;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setMemberConverter(MemberConverter memberConverter) {
        this.memberConverter = memberConverter;
    }

    @Autowired
    public void setSpecificationUtils(SpecificationUtils<MemberEntity> specificationUtils) {
        this.specificationUtils = specificationUtils;
    }

    @Autowired
    public void setPageSpecificationUtils(PageSpecificationUtils<MemberEntity> pageSpecificationUtils) {
        this.pageSpecificationUtils = pageSpecificationUtils;
    }

    @Autowired
    public void setPageUtils(PageUtils<MemberEntity> pageUtils) {
        this.pageUtils = pageUtils;
    }

    @Autowired
    public void setMergeObjectUtils(MergeObjectUtils mergeObjectUtils) {
        this.mergeObjectUtils = mergeObjectUtils;
    }

    @Override
    public Page<MemberResponse> getMembers(int pageIndex, int pageSize, MemberDTO memberDTO) {
        Pageable pageable = pageUtils.getPageable(pageIndex, pageSize);
        List<SearchCriteria> searchCriteria = List.of(
                new SearchCriteria(
                        FieldNameUtils.joinFields(
                                MemberEntity.Fields.userEntity,
                                UserEntity.Fields.fullName
                        ),
                        ComparisonOperator.CONTAINS,
                        Optional.of(memberDTO).map(MemberDTO::getUserEntity).map(UserDTO::getFullName).orElse(null),
                        JoinType.LEFT
                ),
                new SearchCriteria(
                        FieldNameUtils.joinFields(
                                MemberEntity.Fields.memberPackageEntities,
                                MemberPackageEntity.Fields.packageEntity,
                                MemberPackageEntity.Fields.id
                        ),
                        ComparisonOperator.EQUALS,
                        memberDTO.getMemberPackageEntityPackageEntityId(),
                        JoinType.LEFT
                ),
                new SearchCriteria(
                        FieldNameUtils.joinFields(
                                MemberEntity.Fields.memberPackageEntities,
                                MemberPackageEntity.Fields.startDate
                        ),
                        ComparisonOperator.GREATER_THAN_OR_EQUAL_TO,
                        memberDTO.getMemberPackageEntityStartDate(),
                        JoinType.LEFT
                )
        );
        Map<String, SortCriteria> sortCriteriaMap = Map.of(
                FieldNameUtils.joinFields(
                        MemberEntity.Fields.userEntity,
                        UserEntity.Fields.fullName
                ),
                new SortCriteria(
                        memberDTO.getSortFieldName(),
                        AggregationFunction.NONE,
                        memberDTO.getSortDirection()
                        , JoinType.LEFT),
                FieldNameUtils.joinFields(
                        MemberEntity.Fields.memberPackageEntities,
                        MemberPackageEntity.Fields.packageEntity,
                        MemberPackageEntity.Fields.startDate
                ),
                new SortCriteria(
                        memberDTO.getSortFieldName(),
                        AggregationFunction.NONE,
                        memberDTO.getSortDirection(),
                        JoinType.LEFT
                ),
                FieldNameUtils.joinFields(
                        MemberEntity.Fields.memberPackageEntities,
                        MemberPackageEntity.Fields.packageEntity,
                        MemberPackageEntity.Fields.endDate
                ),
                new SortCriteria(
                        memberDTO.getSortFieldName(),
                        AggregationFunction.NONE,
                        memberDTO.getSortDirection(),
                        JoinType.LEFT
                )
        );
        List<SortCriteria> sortCriteria = List.of();
        if (sortCriteriaMap.containsKey(Optional.ofNullable(memberDTO.getSortFieldName()).orElse(""))) {
            sortCriteria = List.of(sortCriteriaMap.get(memberDTO.getSortFieldName()));
        }

        Page<MemberEntity> memberEntityPage = pageSpecificationUtils.getPage(
                specificationUtils.reset()
                        .getSpecifications(searchCriteria, sortCriteria),
                pageable,
                MemberEntity.class
        );
        return memberEntityPage.map(memberConverter::toResponse);
    }

    @Override
    public MemberResponse getMember(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MemberEntity.class, id));
        return memberConverter.toResponse(memberEntity);
    }

    @Override
    public void createMember(MemberDTO memberDTO) {
        // Kiem tra trung email trong db
        if (memberRepository.existsByUserEntityEmail(memberDTO.getUserEntity().getEmail())) {
            throw new ErrorResponse("Email đã tồn tại trong hệ thống", 400);
        }
        // Kiem tra trung phone trong db
        if (memberRepository.existsByUserEntityPhone(memberDTO.getUserEntity().getPhone())) {
            throw new ErrorResponse("Số điện thoại đã tồn tại trong hệ thống", 400);
        }
        // Kiem tra trung idCard trong db
        if (memberRepository.existsByUserEntityIdCard(memberDTO.getUserEntity().getIdCard())) {
            throw new ErrorResponse("Chứng minh nhân dân đã tồn tại trong hệ thống", 400);
        }
        MemberEntity memberEntity = memberConverter.toEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    @Override
    public void updateMember(Long id, MemberDTO memberDTO) {
        MemberEntity existingMemberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MemberEntity.class, id));
        MemberEntity newMemberEntity = memberConverter.toEntity(memberDTO);
        mergeObjectUtils.mergeNonNullFields(newMemberEntity, existingMemberEntity);
        memberRepository.save(existingMemberEntity);
    }

    @Override
    public void deleteMember(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MemberEntity.class, id));
        memberRepository.delete(memberEntity);
    }

    @Override
    public Long countMembers() {
        return memberRepository.count();
    }
}
