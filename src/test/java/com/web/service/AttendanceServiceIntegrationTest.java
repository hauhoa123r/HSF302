package com.web.service;

import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AttendanceServiceIntegrationTest {
    @Autowired
    private AttendanceService attendanceService;

    @Test
    @DisplayName("checkAttendance: valid input")
    void testCheckAttendance_Valid() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        AttendanceResponse response = attendanceService.checkAttendance(dto);
        assertThat(response).isNotNull();
        assertThat(response.getMemberEntityId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("checkAttendance: duplicate id")
    void testCheckAttendance_DuplicateId() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        AttendanceResponse response = attendanceService.checkAttendance(dto);
        AttendanceDTO duplicate = new AttendanceDTO(response.getId(), null, "QR", 1L, Date.valueOf(LocalDate.now()));
        assertThrows(Exception.class, () -> attendanceService.checkAttendance(duplicate));
    }

    @Test
    @DisplayName("getAttendanceByDate: has data")
    void testGetAttendanceByDate_HasData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        attendanceService.checkAttendance(dto);
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(dto);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("getAttendanceByDate: no data")
    void testGetAttendanceByDate_NoData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, null, null, Date.valueOf(LocalDate.now().minusYears(10)));
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(dto);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAttendanceByDate: missing date")
    void testGetAttendanceByDate_MissingDate() {
        AttendanceDTO dto = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.getAttendanceByDate(dto));
    }

    @Test
    @DisplayName("getAttendanceByMonth: has data")
    void testGetAttendanceByMonth_HasData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        attendanceService.checkAttendance(dto);
        List<AttendanceResponse> result = attendanceService.getAttendanceByMonth(dto);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("getAttendanceByMonth: no data")
    void testGetAttendanceByMonth_NoData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, null, null, Date.valueOf(LocalDate.now().minusYears(10)));
        List<AttendanceResponse> result = attendanceService.getAttendanceByMonth(dto);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAttendanceByMonth: missing date")
    void testGetAttendanceByMonth_MissingDate() {
        AttendanceDTO dto = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.getAttendanceByMonth(dto));
    }

    @Test
    @DisplayName("getAttendanceHistoryByMemberId: has data")
    void testGetAttendanceHistoryByMemberId_HasData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        attendanceService.checkAttendance(dto);
        AttendanceDTO query = new AttendanceDTO();
        query.setMemberEntityId(1L);
        List<AttendanceResponse> result = attendanceService.getAttendanceHistoryByMemberId(query);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("getAttendanceHistoryByMemberId: no data")
    void testGetAttendanceHistoryByMemberId_NoData() {
        AttendanceDTO query = new AttendanceDTO();
        query.setMemberEntityId(99999L);
        List<AttendanceResponse> result = attendanceService.getAttendanceHistoryByMemberId(query);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAttendanceHistoryByMemberId: missing memberEntityId")
    void testGetAttendanceHistoryByMemberId_MissingMemberId() {
        AttendanceDTO query = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.getAttendanceHistoryByMemberId(query));
    }

    @Test
    @DisplayName("countAttendanceByMemberId: has data")
    void testCountAttendanceByMemberId_HasData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        attendanceService.checkAttendance(dto);
        AttendanceDTO query = new AttendanceDTO();
        query.setMemberEntityId(1L);
        Long count = attendanceService.countAttendanceByMemberId(query);
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @DisplayName("countAttendanceByMemberId: no data")
    void testCountAttendanceByMemberId_NoData() {
        AttendanceDTO query = new AttendanceDTO();
        query.setMemberEntityId(99999L);
        Long count = attendanceService.countAttendanceByMemberId(query);
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("countAttendanceByMemberId: missing memberEntityId")
    void testCountAttendanceByMemberId_MissingMemberId() {
        AttendanceDTO query = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.countAttendanceByMemberId(query));
    }

    @Test
    @DisplayName("countAttendanceByDate: has data")
    void testCountAttendanceByDate_HasData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        attendanceService.checkAttendance(dto);
        AttendanceDTO query = new AttendanceDTO();
        query.setDate(Date.valueOf(LocalDate.now()));
        Long count = attendanceService.countAttendanceByDate(query);
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @DisplayName("countAttendanceByDate: no data")
    void testCountAttendanceByDate_NoData() {
        AttendanceDTO query = new AttendanceDTO();
        query.setDate(Date.valueOf(LocalDate.now().minusYears(10)));
        Long count = attendanceService.countAttendanceByDate(query);
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("countAttendanceByDate: missing date")
    void testCountAttendanceByDate_MissingDate() {
        AttendanceDTO query = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.countAttendanceByDate(query));
    }

    @Test
    @DisplayName("countAttendanceByMonth: has data")
    void testCountAttendanceByMonth_HasData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        attendanceService.checkAttendance(dto);
        AttendanceDTO query = new AttendanceDTO();
        query.setDate(Date.valueOf(LocalDate.now()));
        Long count = attendanceService.countAttendanceByMonth(query);
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @DisplayName("countAttendanceByMonth: no data")
    void testCountAttendanceByMonth_NoData() {
        AttendanceDTO query = new AttendanceDTO();
        query.setDate(Date.valueOf(LocalDate.now().minusYears(10)));
        Long count = attendanceService.countAttendanceByMonth(query);
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("countAttendanceByMonth: missing date")
    void testCountAttendanceByMonth_MissingDate() {
        AttendanceDTO query = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.countAttendanceByMonth(query));
    }

    @Test
    @DisplayName("getAttendanceByWeek: has data")
    void testGetAttendanceByWeek_HasData() {
        // Dữ liệu mẫu: ngày 2025-05-28 có attendance, tuần này chắc chắn có dữ liệu
        AttendanceDTO dto = new AttendanceDTO();
        dto.setDate(Date.valueOf("2025-05-28"));
        List<AttendanceResponse> result = attendanceService.getAttendanceByWeek(dto);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("getAttendanceByWeek: no data")
    void testGetAttendanceByWeek_NoData() {
        // Tuần của ngày 2020-01-01 chắc chắn không có dữ liệu
        AttendanceDTO dto = new AttendanceDTO();
        dto.setDate(Date.valueOf("2020-01-01"));
        List<AttendanceResponse> result = attendanceService.getAttendanceByWeek(dto);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAttendanceByWeek: missing date")
    void testGetAttendanceByWeek_MissingDate() {
        AttendanceDTO dto = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.getAttendanceByWeek(dto));
    }

    @Test
    @DisplayName("countAttendanceByWeek: has data")
    void testCountAttendanceByWeek_HasData() {
        // Dữ liệu mẫu: ngày 2025-05-28 có attendance, tuần này chắc chắn có dữ liệu
        AttendanceDTO dto = new AttendanceDTO();
        dto.setDate(Date.valueOf("2025-05-28"));
        Long count = attendanceService.countAttendanceByWeek(dto);
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @DisplayName("countAttendanceByWeek: no data")
    void testCountAttendanceByWeek_NoData() {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setDate(Date.valueOf("2020-01-01"));
        Long count = attendanceService.countAttendanceByWeek(dto);
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("countAttendanceByWeek: missing date")
    void testCountAttendanceByWeek_MissingDate() {
        AttendanceDTO dto = new AttendanceDTO();
        assertThrows(Exception.class, () -> attendanceService.countAttendanceByWeek(dto));
    }
}
