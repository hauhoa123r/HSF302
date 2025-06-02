package com.web.service;

import com.web.converter.AttendanceConverter;
import com.web.entity.AttendanceEntity;
import com.web.entity.MemberEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.repository.AttendanceRepository;
import com.web.utils.CheckFieldObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AttendanceServiceUnitTest {

    @MockBean
    private AttendanceRepository attendanceRepository;

    @MockBean
    private AttendanceConverter attendanceConverter;

    @MockBean
    private CheckFieldObject checkFieldObject;

    @Autowired
    private AttendanceService attendanceService;

    private AttendanceDTO attendanceDTO;
    private AttendanceEntity attendanceEntity;
    private AttendanceResponse attendanceResponse;
    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        // Setup test data
        memberEntity = new MemberEntity();
        memberEntity.setId(1L);

        attendanceDTO = new AttendanceDTO();
        attendanceDTO.setId(null);
        attendanceDTO.setMethod("QR");
        attendanceDTO.setMemberEntityId(1L);

        attendanceEntity = new AttendanceEntity();
        attendanceEntity.setId(1L);
        attendanceEntity.setMethod("QR");
        attendanceEntity.setMemberEntity(memberEntity);
        attendanceEntity.setCheckInTime(Timestamp.valueOf("2025-05-28 10:00:00"));

        attendanceResponse = new AttendanceResponse();
        attendanceResponse.setId(1L);
        attendanceResponse.setMethod("QR");
        attendanceResponse.setMemberEntityId(1L);
        attendanceResponse.setCheckInTime(Timestamp.valueOf("2025-05-28 10:00:00"));
    }

    @Test
    @DisplayName("checkAttendance: Should create new attendance successfully")
    void testCheckAttendance_Success() {
        // Given
        doNothing().when(checkFieldObject).check(AttendanceDTO.class, attendanceDTO);
        when(attendanceRepository.existsById(any())).thenReturn(false);
        when(attendanceConverter.toEntity(attendanceDTO)).thenReturn(attendanceEntity);
        when(attendanceRepository.save(any(AttendanceEntity.class))).thenReturn(attendanceEntity);
        when(attendanceConverter.toResponse(attendanceEntity)).thenReturn(attendanceResponse);

        // When
        AttendanceResponse result = attendanceService.checkAttendance(attendanceDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMethod()).isEqualTo("QR");
        assertThat(result.getMemberEntityId()).isEqualTo(1L);

        verify(checkFieldObject).check(AttendanceDTO.class, attendanceDTO);
        verify(attendanceConverter).toEntity(attendanceDTO);
        verify(attendanceRepository).save(any(AttendanceEntity.class));
        verify(attendanceConverter).toResponse(attendanceEntity);
    }

    @Test
    @DisplayName("checkAttendance: Should throw exception when attendance already exists")
    void testCheckAttendance_EntityAlreadyExists() {
        // Given
        attendanceDTO.setId(1L);
        doNothing().when(checkFieldObject).check(AttendanceDTO.class, attendanceDTO);
        when(attendanceRepository.existsById(1L)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> attendanceService.checkAttendance(attendanceDTO))
                .isInstanceOf(EntityAlreadyExistException.class);

        verify(checkFieldObject).check(AttendanceDTO.class, attendanceDTO);
        verify(attendanceRepository).existsById(1L);
        verify(attendanceConverter, never()).toEntity(any());
        verify(attendanceRepository, never()).save(any());
    }

    @Test
    @DisplayName("getAttendanceByDate: Should return attendance list for specific date")
    void testGetAttendanceByDate_Success() {
        // Given
        Date testDate = Date.valueOf("2025-05-28");
        List<AttendanceEntity> entities = Collections.singletonList(attendanceEntity);

        when(attendanceRepository.findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(entities);
        when(attendanceConverter.toResponse(attendanceEntity)).thenReturn(attendanceResponse);

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(testDate);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);

        verify(attendanceRepository).findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class));
        verify(attendanceConverter).toResponse(attendanceEntity);
    }

    @Test
    @DisplayName("getAttendanceByDate: Should return empty list when no attendance found")
    void testGetAttendanceByDate_NoData() {
        // Given
        Date testDate = Date.valueOf("2025-01-01");
        when(attendanceRepository.findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(Collections.emptyList());

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(testDate);

        // Then
        assertThat(result).isEmpty();
        verify(attendanceRepository).findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class));
        verify(attendanceConverter, never()).toResponse(any());
    }

    @Test
    @DisplayName("getAttendanceByWeek: Should return attendance list for specific week")
    void testGetAttendanceByWeek_Success() {
        // Given
        Date testDate = Date.valueOf("2025-05-28");
        List<AttendanceEntity> entities = Collections.singletonList(attendanceEntity);

        when(attendanceRepository.findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(entities);
        when(attendanceConverter.toResponse(attendanceEntity)).thenReturn(attendanceResponse);

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByWeek(testDate);

        // Then
        assertThat(result).hasSize(1);
        verify(attendanceRepository).findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class));
        verify(attendanceConverter).toResponse(attendanceEntity);
    }

    @Test
    @DisplayName("getAttendanceByMonth: Should return attendance list for specific month")
    void testGetAttendanceByMonth_Success() {
        // Given
        Date testDate = Date.valueOf("2025-05-28");
        List<AttendanceEntity> entities = Collections.singletonList(attendanceEntity);

        when(attendanceRepository.findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(entities);
        when(attendanceConverter.toResponse(attendanceEntity)).thenReturn(attendanceResponse);

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByMonth(testDate);

        // Then
        assertThat(result).hasSize(1);
        verify(attendanceRepository).findAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class));
        verify(attendanceConverter).toResponse(attendanceEntity);
    }

    @Test
    @DisplayName("getAttendanceByMemberId: Should return attendance list for specific member")
    void testGetAttendanceByMemberId_Success() {
        // Given
        Long memberId = 1L;
        List<AttendanceEntity> entities = Collections.singletonList(attendanceEntity);

        when(attendanceRepository.findAllByMemberEntityId(memberId)).thenReturn(entities);
        when(attendanceConverter.toResponse(attendanceEntity)).thenReturn(attendanceResponse);

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByMemberId(memberId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMemberEntityId()).isEqualTo(memberId);

        verify(attendanceRepository).findAllByMemberEntityId(memberId);
        verify(attendanceConverter).toResponse(attendanceEntity);
    }

    @Test
    @DisplayName("getAttendanceByMemberId: Should return empty list when member has no attendance")
    void testGetAttendanceByMemberId_NoData() {
        // Given
        Long memberId = 99999L;
        when(attendanceRepository.findAllByMemberEntityId(memberId)).thenReturn(Collections.emptyList());

        // When
        List<AttendanceResponse> result = attendanceService.getAttendanceByMemberId(memberId);

        // Then
        assertThat(result).isEmpty();
        verify(attendanceRepository).findAllByMemberEntityId(memberId);
        verify(attendanceConverter, never()).toResponse(any());
    }

    @Test
    @DisplayName("countAttendanceByMemberId: Should return correct count")
    void testCountAttendanceByMemberId_Success() {
        // Given
        Long memberId = 1L;
        Long expectedCount = 5L;
        when(attendanceRepository.countAllByMemberEntityId(memberId)).thenReturn(expectedCount);

        // When
        Long result = attendanceService.countAttendanceByMemberId(memberId);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(attendanceRepository).countAllByMemberEntityId(memberId);
    }

    @Test
    @DisplayName("countAttendanceByMemberId: Should return zero when member has no attendance")
    void testCountAttendanceByMemberId_NoData() {
        // Given
        Long memberId = 99999L;
        when(attendanceRepository.countAllByMemberEntityId(memberId)).thenReturn(0L);

        // When
        Long result = attendanceService.countAttendanceByMemberId(memberId);

        // Then
        assertThat(result).isZero();
        verify(attendanceRepository).countAllByMemberEntityId(memberId);
    }

    @Test
    @DisplayName("countAttendanceByDate: Should return correct count for specific date")
    void testCountAttendanceByDate_Success() {
        // Given
        Date testDate = Date.valueOf("2025-05-28");
        Long expectedCount = 3L;
        when(attendanceRepository.countAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(expectedCount);

        // When
        Long result = attendanceService.countAttendanceByDate(testDate);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(attendanceRepository).countAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    @DisplayName("countAttendanceByWeek: Should return correct count for specific week")
    void testCountAttendanceByWeek_Success() {
        // Given
        Date testDate = Date.valueOf("2025-05-28");
        Long expectedCount = 10L;
        when(attendanceRepository.countAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(expectedCount);

        // When
        Long result = attendanceService.countAttendanceByWeek(testDate);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(attendanceRepository).countAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    @DisplayName("countAttendanceByMonth: Should return correct count for specific month")
    void testCountAttendanceByMonth_Success() {
        // Given
        Date testDate = Date.valueOf("2025-05-28");
        Long expectedCount = 50L;
        when(attendanceRepository.countAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(expectedCount);

        // When
        Long result = attendanceService.countAttendanceByMonth(testDate);

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(attendanceRepository).countAllByCheckInTimeBetween(any(Timestamp.class), any(Timestamp.class));
    }

    @Test
    @DisplayName("checkAttendance: Should set current timestamp when creating attendance")
    void testCheckAttendance_SetsCurrentTimestamp() {
        // Given
        doNothing().when(checkFieldObject).check(AttendanceDTO.class, attendanceDTO);
        when(attendanceRepository.existsById(any())).thenReturn(false);
        when(attendanceConverter.toEntity(attendanceDTO)).thenReturn(attendanceEntity);
        when(attendanceConverter.toResponse(any(AttendanceEntity.class))).thenReturn(attendanceResponse);

        AttendanceEntity savedEntity = new AttendanceEntity();
        savedEntity.setId(1L);
        savedEntity.setMethod("QR");
        savedEntity.setMemberEntity(memberEntity);

        when(attendanceRepository.save(any(AttendanceEntity.class))).thenAnswer(invocation -> {
            AttendanceEntity entity = invocation.getArgument(0);
            assertThat(entity.getCheckInTime()).isNotNull();
            assertThat(entity.getCheckInTime().getTime()).isCloseTo(System.currentTimeMillis(), within(5000L));
            return savedEntity;
        });

        // When
        AttendanceResponse result = attendanceService.checkAttendance(attendanceDTO);

        // Then
        assertThat(result).isNotNull();
        verify(attendanceRepository).save(any(AttendanceEntity.class));
    }

}
