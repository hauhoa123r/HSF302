package com.web.service.impl;

import com.web.converter.AttendanceConverter;
import com.web.entity.AttendanceEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.model.dto.AttendanceDTO;
import com.web.model.response.AttendanceResponse;
import com.web.repository.AttendanceRepository;
import com.web.service.AttendanceService;
import com.web.utils.CalculatorTimestamp;
import com.web.utils.CheckFieldObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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
    public List<AttendanceResponse> getAttendanceByDate(AttendanceDTO attendanceDTO) {
        checkFieldObject.check(AttendanceDTO.class, attendanceDTO, "date");

        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(attendanceDTO.getDate());

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfDay(),
                calculatorTimestamp.getEndOfDay());

        return attendanceEntities.stream().map(attendanceConverter::toResponse).toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceByMonth(AttendanceDTO attendanceDTO) {
        checkFieldObject.check(AttendanceDTO.class, attendanceDTO, "date");

        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(attendanceDTO.getDate());

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfMonth(),
                calculatorTimestamp.getEndOfMonth());

        return attendanceEntities.stream().map(attendanceConverter::toResponse).toList();
    }

    @Override
    public List<AttendanceResponse> getAttendanceHistoryByMemberId(AttendanceDTO attendanceDTO) {
        checkFieldObject.check(AttendanceDTO.class, attendanceDTO, "memberEntityId");

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByMemberEntityId(attendanceDTO.getMemberEntityId());

        return attendanceEntities.stream()
                .map(attendanceConverter::toResponse)
                .toList();
    }

    @Override
    public Long countAttendanceByMemberId(AttendanceDTO attendanceDTO) {
        checkFieldObject.check(AttendanceDTO.class, attendanceDTO, "memberEntityId");

        return attendanceRepository.countAllByMemberEntityId(attendanceDTO.getMemberEntityId());
    }

    @Override
    public Long countAttendanceByDate(AttendanceDTO attendanceDTO) {
        checkFieldObject.check(AttendanceDTO.class, attendanceDTO, "date");

        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(attendanceDTO.getDate());

        return attendanceRepository.countAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfDay(),
                calculatorTimestamp.getEndOfDay());
    }

    @Override
    public Long countAttendanceByMonth(AttendanceDTO attendanceDTO) {
        checkFieldObject.check(AttendanceDTO.class, attendanceDTO, "date");

        CalculatorTimestamp calculatorTimestamp = new CalculatorTimestamp(attendanceDTO.getDate());

        return attendanceRepository.countAllByCheckInTimeBetween(
                calculatorTimestamp.getStartOfMonth(),
                calculatorTimestamp.getEndOfMonth());
    }


}
