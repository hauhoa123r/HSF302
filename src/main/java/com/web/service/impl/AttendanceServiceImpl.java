package com.web.service.impl;

import com.web.converter.AttendanceConverter;
import com.web.entity.AttendanceEntity;
import com.web.entity.MemberEntity;
import com.web.entity.UserEntity;
import com.web.enums.operation.ComparisonOperator;
import com.web.enums.operation.SortDirection;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.model.dto.AttendanceDTO;
import com.web.model.dto.MemberDTO;
import com.web.model.dto.UserDTO;
import com.web.model.response.AttendanceResponse;
import com.web.repository.AttendanceRepository;
import com.web.service.AttendanceService;
import com.web.utils.*;
import com.web.utils.specification.SpecificationUtils;
import com.web.utils.specification.search.SearchCriteria;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private AttendanceRepository attendanceRepository;
    private AttendanceConverter attendanceConverter;
    private CheckFieldObject checkFieldObject;
    private TimestampUtils timestampUtils;
    private SpecificationUtils<AttendanceEntity> specificationUtils;
    private PageUtils<AttendanceEntity> pageUtils;

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

    @Autowired
    public void setTimestampUtils(TimestampUtils timestampUtils) {
        this.timestampUtils = timestampUtils;
    }

    @Autowired
    public void setSpecificationUtils(SpecificationUtils<AttendanceEntity> specificationUtils) {
        this.specificationUtils = specificationUtils;
    }

    @Autowired
    public void setPageUtils(PageUtils<AttendanceEntity> pageUtils) {
        this.pageUtils = pageUtils;
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
    public Map<String, Long> getAttendanceCountByTimeToday() {
        Map<String, Long> attendanceCountMap = new LinkedHashMap<>();
        timestampUtils.setTime(Date.valueOf(LocalDate.now()));
        timestampUtils.setTimestamp(timestampUtils.plusHours(5));
        for (int i = 0; i < 6; i++) {
            Timestamp startTime = timestampUtils.getTimestamp();
            int startHour = timestampUtils.getHour();
            timestampUtils.setTime(timestampUtils.plusHours(3));
            Timestamp endTime = timestampUtils.getTimestamp();
            int endHour = timestampUtils.getHour();
            Long count = attendanceRepository.countAllByCheckInTimeBetween(startTime, endTime);
            attendanceCountMap.put(String.format("%d-%dh", startHour, endHour), count);
        }
        return attendanceCountMap;
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

    @Override
    public void checkInMember(Long memberId) {
        if (!attendanceRepository.existsByMemberEntityIdAndCheckInTimeBetween(
                memberId,
                timestampUtils.getStartOfDay(),
                timestampUtils.getEndOfDay())) {
            AttendanceDTO attendanceDTO = new AttendanceDTO();
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setId(memberId);
            attendanceDTO.setMemberEntity(memberDTO);
            attendanceDTO.setCheckInTime(new Timestamp(System.currentTimeMillis()));
            checkAttendance(attendanceDTO);
        }
    }

    @Override
    public void checkOutMember(Long memberId) {
        if (attendanceRepository.existsByMemberEntityIdAndCheckInTimeBetween(
                memberId,
                timestampUtils.getStartOfDay(),
                timestampUtils.getEndOfDay())) {
            AttendanceEntity attendanceEntity = attendanceRepository.findAllByMemberEntityId(memberId).stream()
                    .filter(attendance -> attendance.getCheckOutTime() == null)
                    .findFirst()
                    .orElseThrow(() -> new EntityAlreadyExistException(AttendanceEntity.class));

            attendanceEntity.setCheckOutTime(new Timestamp(System.currentTimeMillis()));
            attendanceRepository.save(attendanceEntity);
        }
    }

    @Override
    public Page<AttendanceResponse> getAttendances(int page, int size, AttendanceDTO attendanceDTO) {
        Sort sort = Sort.unsorted();
        if (attendanceDTO.getSortFieldName() != null && attendanceDTO.getSortDirection() != null) {
            sort = Sort.by(attendanceDTO.getSortDirection() == SortDirection.ASC
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC, attendanceDTO.getSortFieldName());
        }
        Pageable pageable = pageUtils.getPageable(page, size, sort);
        List<SearchCriteria> searchCriteria = List.of(
                new SearchCriteria(
                        FieldNameUtils.joinFields(
                                AttendanceEntity.Fields.memberEntity,
                                MemberEntity.Fields.id
                        ),
                        ComparisonOperator.EQUALS, Optional.of(attendanceDTO)
                        .map(AttendanceDTO::getMemberEntity)
                        .map(MemberDTO::getId).orElse(null), JoinType.INNER),
                new SearchCriteria(
                        FieldNameUtils.joinFields(
                                AttendanceEntity.Fields.memberEntity,
                                MemberEntity.Fields.userEntity,
                                UserEntity.Fields.fullName
                        ),
                        ComparisonOperator.CONTAINS, Optional.of(attendanceDTO)
                        .map(AttendanceDTO::getMemberEntity)
                        .map(MemberDTO::getUserEntity)
                        .map(UserDTO::getFullName).orElse(null), JoinType.INNER),
                new SearchCriteria(
                        AttendanceEntity.Fields.checkInTime,
                        ComparisonOperator.GREATER_THAN_OR_EQUAL_TO,
                        attendanceDTO.getCheckInTime(),
                        JoinType.INNER),
                new SearchCriteria(
                        AttendanceEntity.Fields.checkInTime,
                        ComparisonOperator.LESS_THAN_OR_EQUAL_TO,
                        attendanceDTO.getCheckOutTime(),
                        JoinType.INNER)
        );
        Page<AttendanceEntity> attendanceEntityPage = attendanceRepository.findAll(specificationUtils.reset()
                .getSearchSpecifications(searchCriteria), pageable);
        pageUtils.validatePage(attendanceEntityPage, AttendanceEntity.class);
        return attendanceEntityPage.map(attendanceConverter::toResponse);
    }
}
