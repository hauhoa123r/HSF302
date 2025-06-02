package com.web.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.converter.AttendanceConverter;
import com.web.entity.AttendanceEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.repository.AttendanceRepository;
import com.web.service.AttendanceService;
import com.web.utils.CalculatorTimestamp;
import com.web.utils.CheckFieldObject;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private AttendanceRepository attendanceRepository;
    private AttendanceConverter attendanceConverter;
    private CheckFieldObject checkFieldObject;

    @Autowired
    public void setAttendanceRepository(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Autowired
    public void setAttendanceConverter(AttendanceConverter attendanceConverter) {
        this.attendanceConverter = attendanceConverter;
    }

    @Autowired
    public void setCheckFieldObject(CheckFieldObject checkFieldObject) {
        this.checkFieldObject = checkFieldObject;
    }

    @Override
    public AttendanceResponse checkAttendance(AttendanceDTO attendanceDTO) {
        checkFieldObject.check(AttendanceDTO.class, attendanceDTO);

        if (attendanceDTO.getId() != null && attendanceRepository.existsById(attendanceDTO.getId())) {
            throw new EntityAlreadyExistException(AttendanceEntity.class);
        }

        AttendanceEntity attendanceEntity = attendanceConverter.toEntity(attendanceDTO);

        attendanceEntity.setCheckInTime(new Timestamp(System.currentTimeMillis()));

        attendanceEntity = attendanceRepository.save(attendanceEntity);

        return attendanceConverter.toResponse(attendanceEntity);
    }

    @Override
    public List<AttendanceResponse> getAttendanceByDate(Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfDay(),
                calculatorTimestamp.getEndOfDay());

        return attendanceEntities.stream().map(attendanceConverter::toResponse).toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByWeek(Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfWeek(),
                calculatorTimestamp.getEndOfWeek());

        return attendanceEntities.stream().map(attendanceConverter::toResponse).toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByMonth(Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfMonth(),
                calculatorTimestamp.getEndOfMonth());

        return attendanceEntities.stream().map(attendanceConverter::toResponse).toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByMemberId(Long memberId) {
        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByMemberEntityId(memberId);

        return attendanceEntities.stream()
                .map(attendanceConverter::toResponse)
                .toList();
    }

    @Override
    public Long countAttendanceByMemberId(Long memberId) {
        return attendanceRepository.countAllByMemberEntityId(memberId);
    }

    @Override
    public Long countAttendanceByDate(Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        return attendanceRepository.countAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfDay(),
                calculatorTimestamp.getEndOfDay());
    }

    @Override
    public Long countAttendanceByWeek(Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        return attendanceRepository.countAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfWeek(),
                calculatorTimestamp.getEndOfWeek());
    }

    @Override
    public Long countAttendanceByMonth(Date date) {
        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(date);

        return attendanceRepository.countAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfMonth(),
                calculatorTimestamp.getEndOfMonth());
    }


}
