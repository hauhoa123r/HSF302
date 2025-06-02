package com.web.service;

import java.sql.Date;
import java.util.List;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;

public interface AttendanceService {

    AttendanceResponse checkAttendance(AttendanceDTO attendanceDTO);

    List<AttendanceResponse> getAttendanceByDate(Date date);

    List<AttendanceResponse> getAttendanceByWeek(Date date);

    List<AttendanceResponse> getAttendanceByMonth(Date date);

    List<AttendanceResponse> getAttendanceByMemberId(Long memberId);

    Long countAttendanceByMemberId(Long memberId);

    Long countAttendanceByDate(Date date);

    Long countAttendanceByWeek(Date date);

    Long countAttendanceByMonth(Date date);
}
