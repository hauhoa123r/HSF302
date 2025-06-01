package com.web.service;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;

import java.util.List;

public interface AttendanceService {

    AttendanceResponse checkAttendance(AttendanceDTO attendanceDTO);

    List<AttendanceResponse> getAttendanceByDate(AttendanceDTO attendanceDTO);

    List<AttendanceResponse> getAttendanceByWeek(AttendanceDTO attendanceDTO);

    List<AttendanceResponse> getAttendanceByMonth(AttendanceDTO attendanceDTO);

    List<AttendanceResponse> getAttendanceHistoryByMemberId(AttendanceDTO attendanceDTO);

    Long countAttendanceByMemberId(AttendanceDTO attendanceDTO);

    Long countAttendanceByDate(AttendanceDTO attendanceDTO);

    Long countAttendanceByWeek(AttendanceDTO attendanceDTO);

    Long countAttendanceByMonth(AttendanceDTO attendanceDTO);
}
