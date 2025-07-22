package com.web.model.dto;

import com.web.enums.operation.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AttendanceDTO {
    private Long id;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private String method;
    private MemberDTO memberEntity;
    private String sortFieldName;
    private SortDirection sortDirection;
}
