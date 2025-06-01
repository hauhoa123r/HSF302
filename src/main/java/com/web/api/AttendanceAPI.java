package com.web.api;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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

    public AttendanceResponse checkAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.checkAttendance(attendanceDTO);
    }

    public List<AttendanceResponse> getAttendanceByDate(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendanceByDate(attendanceDTO);
    }

    public List<AttendanceResponse> getAttendanceByWeek(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendanceByWeek(attendanceDTO);
    }

    public List<AttendanceResponse> getAttendanceByMonth(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendanceByMonth(attendanceDTO);
    }

    public List<AttendanceResponse> getAttendanceByMemberId(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.getAttendanceHistoryByMemberId(attendanceDTO);
    }

    public Long countAttendanceByMemberId(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.countAttendanceByMemberId(attendanceDTO);
    }

    public Long countAttendanceByDate(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.countAttendanceByDate(attendanceDTO);
    }

    public Long countAttendanceByWeek(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.countAttendanceByWeek(attendanceDTO);
    }

    public Long countAttendanceByMonth(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.countAttendanceByMonth(attendanceDTO);
    }
}
