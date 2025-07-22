package com.web.utils.specification.sort;

import jakarta.persistence.criteria.JoinType;
import lombok.*;
import com.web.enums.operation.AggregationFunction;
import com.web.enums.operation.SortDirection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SortCriteria {
    private String fieldName;
    private AggregationFunction aggregationFunction;
    private SortDirection sortDirection;
    private JoinType joinType;
}
