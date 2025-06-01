package com.web.api;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceAPI {

    private AttendanceService attendanceService;

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    public AttendanceResponse checkAttendance(AttendanceDTO attendanceDTO) {
        return attendanceService.checkAttendance(attendanceDTO);
    }

    public List<AttendanceResponse> getAttendanceByDate(AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendanceByDate(attendanceDTO);
    }

    public List<AttendanceResponse> getAttendanceByMonth(AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendanceByMonth(attendanceDTO);
    }

    public List<AttendanceResponse> getAttendanceByMemberId(AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendanceHistoryByMemberId(attendanceDTO);
    }

    public Long countAttendanceByMemberId(AttendanceDTO attendanceDTO) {
        return attendanceService.countAttendanceByMemberId(attendanceDTO);
    }

    public Long countAttendanceByDate(AttendanceDTO attendanceDTO) {
        return attendanceService.countAttendanceByDate(attendanceDTO);
    }

    public Long countAttendanceByMonth(AttendanceDTO attendanceDTO) {
        return attendanceService.countAttendanceByMonth(attendanceDTO);
    }
}
