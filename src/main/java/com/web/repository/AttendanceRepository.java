package com.web.repository;

import com.web.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Timestamp;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long>, JpaSpecificationExecutor<AttendanceEntity> {

    List<AttendanceEntity> findAllByCheckInTimeBetween(Timestamp checkInTimeAfter, Timestamp checkInTimeBefore);

    List<AttendanceEntity> findAllByMemberEntityId(Long memberEntityId);

    Long countAllByMemberEntityId(Long memberEntityId);

    Long countAllByCheckInTimeBetween(Timestamp checkInTimeAfter, Timestamp checkInTimeBefore);

    boolean existsByMemberEntityIdAndCheckInTimeBetween(Long memberEntityId, Timestamp checkInTimeAfter, Timestamp checkInTimeBefore);
}
