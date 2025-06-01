package com.web.service;

import com.web.converter.AttendanceConverter;
import com.web.entity.AttendanceEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.repository.AttendanceRepository;
import com.web.service.impl.AttendanceServiceImpl;
import com.web.utils.CheckFieldObject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AttendanceServiceUnitTest {
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private AttendanceConverter attendanceConverter;
    @Mock
    private CheckFieldObject checkFieldObject;
    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Test
    @DisplayName("checkAttendance: new attendance")
    void testCheckAttendance_New() {
        AttendanceDTO dto = new AttendanceDTO(null, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        AttendanceEntity entity = new AttendanceEntity();
        AttendanceResponse response = new AttendanceResponse();
        when(attendanceConverter.toEntity(any())).thenReturn(entity);
        when(attendanceRepository.save(any())).thenReturn(entity);
        when(attendanceConverter.toResponse(any())).thenReturn(response);
        AttendanceResponse result = attendanceService.checkAttendance(dto);
        assertThat(result).isNotNull();
        verify(attendanceRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("checkAttendance: already exists")
    void testCheckAttendance_AlreadyExists() {
        AttendanceDTO dto = new AttendanceDTO(1L, null, "QR", 1L, Date.valueOf(LocalDate.now()));
        when(attendanceRepository.existsById(1L)).thenReturn(true);
        assertThrows(EntityAlreadyExistException.class, () -> attendanceService.checkAttendance(dto));
    }

    @Test
    @DisplayName("checkAttendance: missing field")
    void testCheckAttendance_MissingField() {
        AttendanceDTO dto = new AttendanceDTO();
        doThrow(new RuntimeException("Missing field")).when(checkFieldObject).check(any(), any());
        assertThrows(RuntimeException.class, () -> attendanceService.checkAttendance(dto));
    }

    @Test
    @DisplayName("getAttendanceByDate: valid date, has data")
    void testGetAttendanceByDate_HasData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, null, null, Date.valueOf(LocalDate.now()));
        AttendanceEntity entity = new AttendanceEntity();
        AttendanceResponse response = new AttendanceResponse();
        when(attendanceRepository.findAllByCheckInTimeBetween(any(), any())).thenReturn(List.of(entity));
        when(attendanceConverter.toResponse(any())).thenReturn(response);
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(dto);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("getAttendanceByDate: valid date, no data")
    void testGetAttendanceByDate_NoData() {
        AttendanceDTO dto = new AttendanceDTO(null, null, null, null, Date.valueOf(LocalDate.now()));
        when(attendanceRepository.findAllByCheckInTimeBetween(any(), any())).thenReturn(Collections.emptyList());
        List<AttendanceResponse> result = attendanceService.getAttendanceByDate(dto);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getAttendanceByDate: missing date")
    void testGetAttendanceByDate_MissingDate() {
        AttendanceDTO dto = new AttendanceDTO();
        doThrow(new RuntimeException("Missing date")).when(checkFieldObject).check(any(), any(), any());
        assertThrows(RuntimeException.class, () -> attendanceService.getAttendanceByDate(dto));
    }

    // Có thể bổ sung thêm các test cho getAttendanceByMonth, getAttendanceHistoryByMemberId, countAttendanceByMemberId, countAttendanceByDate, countAttendanceByMonth tương tự
}

