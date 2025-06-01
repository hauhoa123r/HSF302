package com.web.repository;

import com.web.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    List<AttendanceEntity> findAllByCheckInTimeBetween(Timestamp checkInTimeAfter, Timestamp checkInTimeBefore);

    List<AttendanceEntity> findAllByMemberEntityId(Long memberEntityId);

    Long countAllByMemberEntityId(Long memberEntityId);

    Long countAllByCheckInTimeBetween(Timestamp checkInTimeAfter, Timestamp checkInTimeBefore);
}
