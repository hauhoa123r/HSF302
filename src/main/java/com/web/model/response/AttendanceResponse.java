package com.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AttendanceResponse {
    private Long id;
    private MemberResponse memberEntity;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private String method;
}
