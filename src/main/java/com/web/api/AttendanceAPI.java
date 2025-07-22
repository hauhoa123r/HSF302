package com.web.api;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
public class AttendanceAPI {

    private AttendanceService attendanceService;

    @Autowired
    public void setAttendanceService(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/api/attendance")
    public AttendanceResponse checkAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.checkAttendance(attendanceDTO);
    }

    @GetMapping("/api/attendance/date/{date}")
    public List<AttendanceResponse> getAttendanceByDate(@PathVariable Date date) {
        return attendanceService.getAttendanceByDate(date);
    }

    @GetMapping("/api/attendance/week/{date}")
    public List<AttendanceResponse> getAttendanceByWeek(@PathVariable Date date) {
        return attendanceService.getAttendanceByWeek(date);
    }

    @GetMapping("/api/attendance/month/{date}")
    public List<AttendanceResponse> getAttendanceByMonth(@PathVariable Date date) {
        return attendanceService.getAttendanceByMonth(date);
    }

    @GetMapping("/api/attendance/member/{memberId}")
    public List<AttendanceResponse> getAttendanceByMemberId(@PathVariable Long memberId) {
        return attendanceService.getAttendanceByMemberId(memberId);
    }

    @GetMapping("/api/attendance/count/member/{memberId}")
    public Long countAttendanceByMemberId(@PathVariable Long memberId) {
        return attendanceService.countAttendanceByMemberId(memberId);
    }

    @GetMapping("/api/attendance/count/date/{date}")
    public Long countAttendanceByDate(@PathVariable Date date) {
        return attendanceService.countAttendanceByDate(date);
    }

    @GetMapping("/api/attendance/count/week/{date}")
    public Long countAttendanceByWeek(@PathVariable Date date) {
        return attendanceService.countAttendanceByWeek(date);
    }

    @GetMapping("/api/attendance/count/month/{date}")
    public Long countAttendanceByMonth(@PathVariable Date date) {
        return attendanceService.countAttendanceByMonth(date);
    }

    @GetMapping("/api/admin/attendance/page/{pageIndex}")
    public Map<String, Object> getAttendanceForAdmin(@PathVariable int pageIndex, @ModelAttribute AttendanceDTO attendanceDTO) {
        Page<AttendanceResponse> attendanceResponsePage = attendanceService.getAttendances(pageIndex, 6, attendanceDTO);
        return Map.of(
                "items", attendanceResponsePage.getContent(),
                "currentPage", attendanceResponsePage.getNumber(),
                "totalPages", attendanceResponsePage.getTotalPages()
        );
    }
}
