package com.web.service.impl;

import com.web.converter.PromotionConverter;
import com.web.entity.PromotionEntity;
import com.web.enums.operation.AggregationFunction;
import com.web.enums.operation.ComparisonOperator;
import com.web.enums.operation.LogicalOperator;
import com.web.enums.operation.SortDirection;
import com.web.model.response.PromotionResponse;
import com.web.repository.PromotionRepository;
import com.web.service.PromotionService;
import com.web.utils.specification.SpecificationUtils;
import com.web.utils.specification.search.SearchCriteria;
import com.web.utils.specification.sort.SortCriteria;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    private PromotionRepository promotionRepository;
    private PromotionConverter promotionConverter;
    private SpecificationUtils<PromotionEntity> specificationUtils;

    @Autowired
    public void setPromotionRepository(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Autowired
    public void setPromotionConverter(PromotionConverter promotionConverter) {
        this.promotionConverter = promotionConverter;
    }

    @Autowired
    public void setSpecificationUtils(SpecificationUtils<PromotionEntity> specificationUtils) {
        this.specificationUtils = specificationUtils;
    }

    @Override
    public List<PromotionResponse> getPromotionsByPackage(Long packageId) {
        return promotionRepository.findAll(
                        specificationUtils.reset().
                                addSearchCriteria(new SearchCriteria("packageEntity.id", ComparisonOperator.EQUALS, packageId, JoinType.INNER), LogicalOperator.AND)
                                .addSearchCriteria(new SearchCriteria("status", ComparisonOperator.EQUALS, "ACTIVE", JoinType.INNER), LogicalOperator.AND)
                                .addSearchCriteria(new SearchCriteria("remainingUsage", ComparisonOperator.GREATER_THAN, 0, JoinType.INNER), LogicalOperator.AND)
                                .addSortCriteria(new SortCriteria("discountPercent", AggregationFunction.NONE, SortDirection.DESC, JoinType.INNER), LogicalOperator.AND)
                                .getSpecification()
                ).
                stream()
                .map(promotionConverter::toResponse).toList();
    }
}
