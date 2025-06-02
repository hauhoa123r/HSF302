package com.web.api;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceAPI {

    private AttendanceService attendanceService;

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping()
    public AttendanceResponse checkAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.checkAttendance(attendanceDTO);
    }

    @GetMapping("/date/{date}")
    public List<AttendanceResponse> getAttendanceByDate(@PathVariable Date date) {
        return attendanceService.getAttendanceByDate(date);
    }

    @GetMapping("/week/{date}")
    public List<AttendanceResponse> getAttendanceByWeek(@PathVariable Date date) {
        return attendanceService.getAttendanceByWeek(date);
    }

    @GetMapping("/month/{date}")
    public List<AttendanceResponse> getAttendanceByMonth(@PathVariable Date date) {
        return attendanceService.getAttendanceByMonth(date);
    }

    @GetMapping("/member/{memberId}")
    public List<AttendanceResponse> getAttendanceByMemberId(@PathVariable Long memberId) {
        return attendanceService.getAttendanceByMemberId(memberId);
    }

    @GetMapping("/count/member/{memberId}")
    public Long countAttendanceByMemberId(@PathVariable Long memberId) {
        return attendanceService.countAttendanceByMemberId(memberId);
    }

    @GetMapping("/count/date/{date}")
    public Long countAttendanceByDate(@PathVariable Date date) {
        return attendanceService.countAttendanceByDate(date);
    }

    @GetMapping("/count/week/{date}")
    public Long countAttendanceByWeek(@PathVariable Date date) {
        return attendanceService.countAttendanceByWeek(date);
    }

    @GetMapping("/count/month/{date}")
    public Long countAttendanceByMonth(@PathVariable Date date) {
        return attendanceService.countAttendanceByMonth(date);
    }
}
