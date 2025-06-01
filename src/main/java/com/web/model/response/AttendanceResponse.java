package com.web.model.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AttendanceResponse {

    private Long id;
    private Timestamp checkInTime;
    private String method;
    private Long memberEntityId;
}
