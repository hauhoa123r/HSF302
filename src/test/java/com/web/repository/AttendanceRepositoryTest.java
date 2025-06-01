package com.web.repository;

import com.web.entity.AttendanceEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AttendanceRepositoryTest {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Test
    @DisplayName("findAllByMemberEntityId: has data (db_test)")
    void testFindAllByMemberEntityId_HasData() {
        List<AttendanceEntity> result = attendanceRepository.findAllByMemberEntityId(1L);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("findAllByMemberEntityId: no data (db_test)")
    void testFindAllByMemberEntityId_NoData() {
        List<AttendanceEntity> result = attendanceRepository.findAllByMemberEntityId(99999L);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("countAllByMemberEntityId: has data (db_test)")
    void testCountAllByMemberEntityId_HasData() {
        Long count = attendanceRepository.countAllByMemberEntityId(1L);
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @DisplayName("countAllByMemberEntityId: no data (db_test)")
    void testCountAllByMemberEntityId_NoData() {
        Long count = attendanceRepository.countAllByMemberEntityId(99999L);
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("countAllByCheckInTimeBetween: has data (db_test)")
    void testCountAllByCheckInTimeBetween_HasData() {
        Long count = attendanceRepository.countAllByCheckInTimeBetween(
                Timestamp.valueOf("2025-05-28 00:00:00"),
                Timestamp.valueOf("2025-05-28 23:59:59"));
        assertThat(count).isGreaterThan(0);
    }

    @Test
    @DisplayName("countAllByCheckInTimeBetween: no data (db_test)")
    void testCountAllByCheckInTimeBetween_NoData() {
        Long count = attendanceRepository.countAllByCheckInTimeBetween(
                Timestamp.valueOf("2020-01-01 00:00:00"),
                Timestamp.valueOf("2020-01-01 23:59:59"));
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("findAllByCheckInTimeBetween: has data (db_test)")
    void testFindAllByCheckInTimeBetween_HasData() {
        List<AttendanceEntity> result = attendanceRepository.findAllByCheckInTimeBetween(
                Timestamp.valueOf("2025-05-28 00:00:00"),
                Timestamp.valueOf("2025-05-28 23:59:59"));
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("findAllByCheckInTimeBetween: no data (db_test)")
    void testFindAllByCheckInTimeBetween_NoData() {
        List<AttendanceEntity> result = attendanceRepository.findAllByCheckInTimeBetween(
                Timestamp.valueOf("2020-01-01 00:00:00"),
                Timestamp.valueOf("2020-01-01 23:59:59"));
        assertThat(result).isEmpty();
    }
}
