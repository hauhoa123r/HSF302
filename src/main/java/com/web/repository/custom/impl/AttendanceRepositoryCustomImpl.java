package com.web.repository.custom.impl;

import com.web.repository.custom.AttendanceRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Map;

@Repository
public class AttendanceRepositoryCustomImpl implements AttendanceRepositoryCustom {
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Map<String, Long> getAttendanceCountByTimePeriod(Timestamp startTime, Timestamp endTime) {
        String jpql = """
                SELECT COU
                """;
        return Map.of();
    }
}
