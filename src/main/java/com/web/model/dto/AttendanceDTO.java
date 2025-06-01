package com.web.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AttendanceDTO {

    private Long id;
    private Timestamp checkInTime;
    private String method;
    private Long memberEntityId;
    private Date date;
}
