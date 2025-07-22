package com.web.service;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface AttendanceService {

    AttendanceResponse checkAttendance(AttendanceDTO attendanceDTO);

    List<AttendanceResponse> getAttendanceByDate(Date date);

    List<AttendanceResponse> getAttendanceByWeek(Date date);

    List<AttendanceResponse> getAttendanceByMonth(Date date);

    List<AttendanceResponse> getAttendanceByMemberId(Long memberId);

    Map<String, Long> getAttendanceCountByTimeToday();

    Long countAttendanceByMemberId(Long memberId);

    Long countAttendanceByDate(Date date);

    Long countAttendanceByWeek(Date date);

    Long countAttendanceByMonth(Date date);

    void checkInMember(Long memberId);

    void checkOutMember(Long memberId);

    Page<AttendanceResponse> getAttendances(int page, int size, AttendanceDTO attendanceDTO);
}
