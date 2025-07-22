package com.web.repository.custom;

import java.sql.Timestamp;
import java.util.Map;

public interface AttendanceRepositoryCustom {
    Map<String, Long> getAttendanceCountByTimePeriod(Timestamp startTime, Timestamp endTime);
}
