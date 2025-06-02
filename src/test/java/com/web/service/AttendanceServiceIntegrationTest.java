package com.web.service;

import com.web.entity.AttendanceEntity;
import com.web.entity.MemberEntity;
import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.repository.AttendanceRepository;
import com.web.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AttendanceServiceIntegrationTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MemberRepository memberRepository;

    private MemberEntity testMember;
    private AttendanceDTO testAttendanceDTO;

    @BeforeEach
    void setUp() {
        // Get existing test member from database (member_id = 1 from init.sql)
        Optional<MemberEntity> memberOpt = memberRepository.findById(1L);
        assertThat(memberOpt).isPresent();
        testMember = memberOpt.get();

        testAttendanceDTO = new AttendanceDTO();
        testAttendanceDTO.setMethod("QR");
        testAttendanceDTO.setMemberEntityId(testMember.getId());
    }

    @Test
    @DisplayName("Integration: checkAttendance should create new attendance record")
    void testCheckAttendance_Integration() {
        // Given
        long initialCount = attendanceRepository.count();

        // When
        AttendanceResponse result = attendanceService.checkAttendance(testAttendanceDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMethod()).isEqualTo("QR");
        assertThat(result.getMemberEntityId()).isEqualTo(testMember.getId());
        assertThat(result.getCheckInTime()).isNotNull();

        // Verify in database
        long finalCount = attendanceRepository.count();
        assertThat(finalCount).isEqualTo(initialCount + 1);

        Optional<AttendanceEntity> savedEntity = attendanceRepository.findById(result.getId());
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get().getMethod()).isEqualTo("QR");
        assertThat(savedEntity.get().getMemberEntity().getId()).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("Integration: getAttendanceByDate should return attendance for specific date using test data")
    void testGetAttendanceByDate_WithTestData() {
        // Given - Using date from test data (2025-05-28)
        Date testDate = Date.valueOf("2025-05-28");

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(testDate);

        // Then
        assertThat(result).hasSizeGreaterThan(0);

        // Verify all returned records are from the specified date
        for (AttendanceResponse attendance : result) {
            LocalDate attendanceDate = attendance.getCheckInTime().toLocalDateTime().toLocalDate();
            assertThat(attendanceDate).isEqualTo(testDate.toLocalDate());
        }
    }

    @Test
    @DisplayName("Integration: getAttendanceByDate should return empty list for date with no data")
    void testGetAttendanceByDate_NoData() {
        // Given - Using a date with no test data
        Date testDate = Date.valueOf("2020-01-01");

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(testDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Integration: getAttendanceByWeek should return attendance for specific week")
    void testGetAttendanceByWeek_WithTestData() {
        // Given - Using date from test data week (2025-05-28 is Wednesday)
        Date testDate = Date.valueOf("2025-05-28");

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByWeek(testDate);

        // Then
        assertThat(result).isNotEmpty();

        // Verify all returned records are from the week containing the test date
        LocalDate weekStart = testDate.toLocalDate().with(java.time.temporal.WeekFields.ISO.dayOfWeek(), 1);
        LocalDate weekEnd = testDate.toLocalDate().with(java.time.temporal.WeekFields.ISO.dayOfWeek(), 7);

        for (AttendanceResponse attendance : result) {
            LocalDate attendanceDate = attendance.getCheckInTime().toLocalDateTime().toLocalDate();
            assertThat(attendanceDate).isBetween(weekStart, weekEnd);
        }
    }

    @Test
    @DisplayName("Integration: getAttendanceByMonth should return attendance for specific month")
    void testGetAttendanceByMonth_WithTestData() {
        // Given - Using date from test data (May 2025)
        Date testDate = Date.valueOf("2025-05-28");

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByMonth(testDate);

        // Then
        assertThat(result).isNotEmpty();

        // Verify all returned records are from May 2025
        for (AttendanceResponse attendance : result) {
            LocalDateTime attendanceDateTime = attendance.getCheckInTime().toLocalDateTime();
            assertThat(attendanceDateTime.getYear()).isEqualTo(2025);
            assertThat(attendanceDateTime.getMonthValue()).isEqualTo(5);
        }
    }

    @Test
    @DisplayName("Integration: getAttendanceByMemberId should return attendance for specific member")
    void testGetAttendanceByMemberId_WithTestData() {
        // Given - Using member_id = 1 from test data
        Long memberId = 1L;

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByMemberId(memberId);

        // Then
        assertThat(result).isNotEmpty();

        // Verify all returned records belong to the specified member
        for (AttendanceResponse attendance : result) {
            assertThat(attendance.getMemberEntityId()).isEqualTo(memberId);
        }
    }

    @Test
    @DisplayName("Integration: getAttendanceByMemberId should return empty list for non-existent member")
    void testGetAttendanceByMemberId_NoData() {
        // Given - Using non-existent member_id
        Long memberId = 99999L;

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByMemberId(memberId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Integration: countAttendanceByMemberId should return correct count")
    void testCountAttendanceByMemberId_WithTestData() {
        // Given - Using member_id = 1 from test data
        Long memberId = 1L;

        // When
        Long count = attendanceService.countAttendanceByMemberId(memberId);

        // Then
        assertThat(count).isGreaterThan(0);

        // Verify count matches actual records
        List<AttendanceResponse> actualRecords = attendanceService.getAttendanceByMemberId(memberId);
        assertThat(count).isEqualTo(actualRecords.size());
    }

    @Test
    @DisplayName("Integration: countAttendanceByMemberId should return zero for non-existent member")
    void testCountAttendanceByMemberId_NoData() {
        // Given - Using non-existent member_id
        Long memberId = 99999L;

        // When
        Long count = attendanceService.countAttendanceByMemberId(memberId);

        // Then
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("Integration: countAttendanceByDate should return correct count")
    void testCountAttendanceByDate_WithTestData() {
        // Given - Using date from test data
        Date testDate = Date.valueOf("2025-05-28");

        // When
        Long count = attendanceService.countAttendanceByDate(testDate);

        // Then
        assertThat(count).isGreaterThan(0);

        // Verify count matches actual records
        List<AttendanceResponse> actualRecords = attendanceService.getAttendanceByDate(testDate);
        assertThat(count).isEqualTo(actualRecords.size());
    }

    @Test
    @DisplayName("Integration: countAttendanceByDate should return zero for date with no data")
    void testCountAttendanceByDate_NoData() {
        // Given - Using date with no test data
        Date testDate = Date.valueOf("2020-01-01");

        // When
        Long count = attendanceService.countAttendanceByDate(testDate);

        // Then
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("Integration: countAttendanceByWeek should return correct count")
    void testCountAttendanceByWeek_WithTestData() {
        // Given - Using date from test data week
        Date testDate = Date.valueOf("2025-05-28");

        // When
        Long count = attendanceService.countAttendanceByWeek(testDate);

        // Then
        assertThat(count).isGreaterThan(0);

        // Verify count matches actual records
        List<AttendanceResponse> actualRecords = attendanceService.getAttendanceByWeek(testDate);
        assertThat(count).isEqualTo(actualRecords.size());
    }

    @Test
    @DisplayName("Integration: countAttendanceByMonth should return correct count")
    void testCountAttendanceByMonth_WithTestData() {
        // Given - Using date from test data month
        Date testDate = Date.valueOf("2025-05-28");

        // When
        Long count = attendanceService.countAttendanceByMonth(testDate);

        // Then
        assertThat(count).isGreaterThan(0);

        // Verify count matches actual records
        List<AttendanceResponse> actualRecords = attendanceService.getAttendanceByMonth(testDate);
        assertThat(count).isEqualTo(actualRecords.size());
    }

    @Test
    @DisplayName("Integration: Should handle multiple attendance records correctly")
    void testMultipleAttendanceOperations() {
        // Given - Create multiple attendance records
        AttendanceDTO attendance1 = new AttendanceDTO();
        attendance1.setMethod("QR");
        attendance1.setMemberEntityId(1L);

        AttendanceDTO attendance2 = new AttendanceDTO();
        attendance2.setMethod("Manual");
        attendance2.setMemberEntityId(1L);

        // When - Create multiple attendance records
        AttendanceResponse result1 = attendanceService.checkAttendance(attendance1);
        AttendanceResponse result2 = attendanceService.checkAttendance(attendance2);

        // Then - Verify both records are created
        assertThat(result1).isNotNull();
        assertThat(result2).isNotNull();
        assertThat(result1.getId()).isNotEqualTo(result2.getId());

        // Verify count increases
        Long currentCount = attendanceService.countAttendanceByMemberId(1L);
        assertThat(currentCount).isGreaterThanOrEqualTo(2);

        // Verify both records appear in member's attendance list
        List<AttendanceResponse> memberAttendance = attendanceService.getAttendanceByMemberId(1L);
        assertThat(memberAttendance.stream().anyMatch(a -> a.getId().equals(result1.getId()))).isTrue();
        assertThat(memberAttendance.stream().anyMatch(a -> a.getId().equals(result2.getId()))).isTrue();
    }

    @Test
    @DisplayName("Integration: Should handle edge case dates correctly")
    void testEdgeCaseDates() {
        // Test first day of month
        Date firstOfMonth = Date.valueOf("2025-05-01");
        List<AttendanceResponse> firstDayResults = attendanceService.getAttendanceByDate(firstOfMonth);
        Long firstDayCount = attendanceService.countAttendanceByDate(firstOfMonth);
        assertThat(firstDayCount).isEqualTo(firstDayResults.size());

        // Test last day of month
        Date lastOfMonth = Date.valueOf("2025-05-31");
        List<AttendanceResponse> lastDayResults = attendanceService.getAttendanceByDate(lastOfMonth);
        Long lastDayCount = attendanceService.countAttendanceByDate(lastOfMonth);
        assertThat(lastDayCount).isEqualTo(lastDayResults.size());

        // Test month-end boundary
        List<AttendanceResponse> monthResults = attendanceService.getAttendanceByMonth(firstOfMonth);
        Long monthCount = attendanceService.countAttendanceByMonth(firstOfMonth);
        assertThat(monthCount).isEqualTo(monthResults.size());
    }

    @Test
    @DisplayName("Integration: Should validate timestamp precision in database operations")
    void testTimestampPrecision() {
        // Given
        Timestamp beforeCreation = new Timestamp(System.currentTimeMillis());

        // When
        AttendanceResponse result = attendanceService.checkAttendance(testAttendanceDTO);

        Timestamp afterCreation = new Timestamp(System.currentTimeMillis());

        // Then
        assertThat(result.getCheckInTime()).isNotNull();
        assertThat(result.getCheckInTime()).isBetween(beforeCreation, afterCreation);

        // Verify the timestamp is stored correctly in database
        Optional<AttendanceEntity> savedEntity = attendanceRepository.findById(result.getId());
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get().getCheckInTime()).isEqualTo(result.getCheckInTime());
    }
}
