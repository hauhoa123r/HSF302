package com.web.utils;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTimestampTest {
    @Test
    void testPlusAndMinusDays() {
        Timestamp base = Timestamp.valueOf("2024-06-01 00:00:00");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-06-03 00:00:00"), calc.plusDays(2));
        assertEquals(Timestamp.valueOf("2024-05-30 00:00:00"), calc.minusDays(2));
    }

    @Test
    void testPlusDaysWithNegativeValue() {
        Timestamp base = Timestamp.valueOf("2024-06-01 00:00:00");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-05-30 00:00:00"), calc.plusDays(-2));
    }

    @Test
    void testLeapYear() {
        Timestamp base = Timestamp.valueOf("2024-02-28 00:00:00");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-02-29 00:00:00"), calc.plusDays(1)); // 2024 là năm nhuận
    }

    @Test
    void testEndOfMonth() {
        Timestamp base = Timestamp.valueOf("2024-01-31 10:00:00");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(31, calc.getDay());
        assertEquals(1, calc.getMonth());
        assertEquals(2024, calc.getYear());
        Timestamp nextDay = calc.plusDays(1);
        CalculatorTimestamp calcNext = new CalculatorTimestamp(nextDay);
        assertEquals(1, calcNext.getDay());
        assertEquals(2, calcNext.getMonth());
    }

    @Test
    void testPlusAndMinusHours() {
        Timestamp base = Timestamp.valueOf("2024-06-01 10:00:00");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-06-01 13:00:00"), calc.plusHours(3));
        assertEquals(Timestamp.valueOf("2024-06-01 07:00:00"), calc.minusHours(3));
    }

    @Test
    void testPlusAndMinusMinutes() {
        Timestamp base = Timestamp.valueOf("2024-06-01 10:10:00");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-06-01 10:15:00"), calc.plusMinutes(5));
        assertEquals(Timestamp.valueOf("2024-06-01 10:05:00"), calc.minusMinutes(5));
    }

    @Test
    void testPlusAndMinusSeconds() {
        Timestamp base = Timestamp.valueOf("2024-06-01 10:10:10");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-06-01 10:10:15"), calc.plusSeconds(5));
        assertEquals(Timestamp.valueOf("2024-06-01 10:10:05"), calc.minusSeconds(5));
    }

    @Test
    void testGetDayMonthYear() {
        Timestamp base = Timestamp.valueOf("2024-06-01 10:10:10");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(1, calc.getDay());
        assertEquals(6, calc.getMonth());
        assertEquals(2024, calc.getYear());
    }

    @Test
    void testGetStartAndEndOfDay() {
        Timestamp base = Timestamp.valueOf("2024-06-01 10:10:10");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-06-01 00:00:00"), calc.getStartOfDay());
        Timestamp endOfDay = calc.getEndOfDay();
        assertEquals("2024-06-01 23:59:59.999", endOfDay.toString().substring(0, 23));
    }

    @Test
    void testToSqlDateAndToLocalDateTime() {
        Timestamp base = Timestamp.valueOf("2024-06-01 10:10:10");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        Date sqlDate = calc.toSqlDate();
        assertEquals("2024-06-01", sqlDate.toString());
        LocalDateTime ldt = calc.toLocalDateTime();
        assertEquals(LocalDateTime.of(2024, 6, 1, 10, 10, 10), ldt);
    }

    @Test
    void testIsBeforeIsAfterIsSameDay() {
        CalculatorTimestamp t1 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 10:00:00"));
        CalculatorTimestamp t2 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-02 10:00:00"));
        CalculatorTimestamp t3 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 23:59:59"));
        assertTrue(t1.isBefore(t2));
        assertTrue(t2.isAfter(t1));
        assertTrue(t1.isSameDay(t3));
        assertFalse(t1.isSameDay(t2));
    }

    @Test
    void testIsSameDayWithDifferentTimes() {
        CalculatorTimestamp t1 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 00:00:00"));
        CalculatorTimestamp t2 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 23:59:59"));
        assertTrue(t1.isSameDay(t2));
    }

    @Test
    void testConstructorWithDate() {
        Date date = Date.valueOf("2024-06-01");
        CalculatorTimestamp calc = new CalculatorTimestamp(date);
        assertEquals(Timestamp.valueOf("2024-06-01 00:00:00"), calc.getTimestamp());
    }

    @Test
    void testGetStartAndEndOfMonth() {
        // Tháng 6/2024 có 30 ngày
        Timestamp base = Timestamp.valueOf("2024-06-15 12:34:56");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-06-01 00:00:00"), calc.getStartOfMonth());
        assertEquals(Timestamp.valueOf("2024-06-30 23:59:59.999"), calc.getEndOfMonth());

        // Tháng 2 năm nhuận (2024)
        Timestamp leapFeb = Timestamp.valueOf("2024-02-10 08:00:00");
        CalculatorTimestamp calcLeapFeb = new CalculatorTimestamp(leapFeb);
        assertEquals(Timestamp.valueOf("2024-02-01 00:00:00"), calcLeapFeb.getStartOfMonth());
        assertEquals(Timestamp.valueOf("2024-02-29 23:59:59.999"), calcLeapFeb.getEndOfMonth());

        // Tháng 2 không nhuận (2023)
        Timestamp feb2023 = Timestamp.valueOf("2023-02-10 08:00:00");
        CalculatorTimestamp calcFeb2023 = new CalculatorTimestamp(feb2023);
        assertEquals(Timestamp.valueOf("2023-02-01 00:00:00"), calcFeb2023.getStartOfMonth());
        assertEquals(Timestamp.valueOf("2023-02-28 23:59:59.999"), calcFeb2023.getEndOfMonth());

        // Tháng 12 cuối năm
        Timestamp dec = Timestamp.valueOf("2024-12-15 08:00:00");
        CalculatorTimestamp calcDec = new CalculatorTimestamp(dec);
        assertEquals(Timestamp.valueOf("2024-12-01 00:00:00"), calcDec.getStartOfMonth());
        assertEquals(Timestamp.valueOf("2024-12-31 23:59:59.999"), calcDec.getEndOfMonth());
    }

    @Test
    void testGetStartAndEndOfYear() {
        // Năm 2024 là năm nhuận
        Timestamp base = Timestamp.valueOf("2024-06-15 12:34:56");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-01-01 00:00:00"), calc.getStartOfYear());
        assertEquals(Timestamp.valueOf("2024-12-31 23:59:59.999"), calc.getEndOfYear());

        // Năm 2023 không nhuận
        Timestamp base2023 = Timestamp.valueOf("2023-03-10 10:00:00");
        CalculatorTimestamp calc2023 = new CalculatorTimestamp(base2023);
        assertEquals(Timestamp.valueOf("2023-01-01 00:00:00"), calc2023.getStartOfYear());
        assertEquals(Timestamp.valueOf("2023-12-31 23:59:59.999"), calc2023.getEndOfYear());

        // Đầu năm
        Timestamp jan = Timestamp.valueOf("2024-01-01 00:00:00");
        CalculatorTimestamp calcJan = new CalculatorTimestamp(jan);
        assertEquals(Timestamp.valueOf("2024-01-01 00:00:00"), calcJan.getStartOfYear());
        assertEquals(Timestamp.valueOf("2024-12-31 23:59:59.999"), calcJan.getEndOfYear());

        // Cuối năm
        Timestamp dec = Timestamp.valueOf("2024-12-31 23:59:59.999");
        CalculatorTimestamp calcDec = new CalculatorTimestamp(dec);
        assertEquals(Timestamp.valueOf("2024-01-01 00:00:00"), calcDec.getStartOfYear());
        assertEquals(Timestamp.valueOf("2024-12-31 23:59:59.999"), calcDec.getEndOfYear());
    }

    @Test
    void testGetStartAndEndOfWeek() {
        // Thứ 4, 2024-06-05
        Timestamp base = Timestamp.valueOf("2024-06-05 15:00:00");
        CalculatorTimestamp calc = new CalculatorTimestamp(base);
        assertEquals(Timestamp.valueOf("2024-06-03 00:00:00"), calc.getStartOfWeek()); // Thứ Hai
        assertEquals(Timestamp.valueOf("2024-06-09 23:59:59.999"), calc.getEndOfWeek()); // Chủ Nhật

        // Chủ Nhật
        Timestamp sunday = Timestamp.valueOf("2024-06-09 10:00:00");
        CalculatorTimestamp calcSunday = new CalculatorTimestamp(sunday);
        assertEquals(Timestamp.valueOf("2024-06-03 00:00:00"), calcSunday.getStartOfWeek());
        assertEquals(Timestamp.valueOf("2024-06-09 23:59:59.999"), calcSunday.getEndOfWeek());

        // Thứ Hai
        Timestamp monday = Timestamp.valueOf("2024-06-03 01:00:00");
        CalculatorTimestamp calcMonday = new CalculatorTimestamp(monday);
        assertEquals(Timestamp.valueOf("2024-06-03 00:00:00"), calcMonday.getStartOfWeek());
        assertEquals(Timestamp.valueOf("2024-06-09 23:59:59.999"), calcMonday.getEndOfWeek());
    }

    @Test
    void testGetDayOfWeek() {
        // Thứ Hai
        CalculatorTimestamp mon = new CalculatorTimestamp(Timestamp.valueOf("2024-06-03 10:00:00"));
        assertEquals(1, mon.getDayOfWeek());
        // Thứ Bảy
        CalculatorTimestamp sat = new CalculatorTimestamp(Timestamp.valueOf("2024-06-08 10:00:00"));
        assertEquals(6, sat.getDayOfWeek());
        // Chủ Nhật
        CalculatorTimestamp sun = new CalculatorTimestamp(Timestamp.valueOf("2024-06-09 10:00:00"));
        assertEquals(7, sun.getDayOfWeek());
    }

    @Test
    void testGetDayOfYear() {
        CalculatorTimestamp first = new CalculatorTimestamp(Timestamp.valueOf("2024-01-01 00:00:00"));
        assertEquals(1, first.getDayOfYear());
        CalculatorTimestamp mid = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 00:00:00"));
        assertEquals(153, mid.getDayOfYear());
        CalculatorTimestamp last = new CalculatorTimestamp(Timestamp.valueOf("2024-12-31 23:59:59"));
        assertEquals(366, last.getDayOfYear()); // 2024 là năm nhuận
    }

    @Test
    void testFormat() {
        CalculatorTimestamp calc = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 15:45:30"));
        assertEquals("2024/06/01", calc.format("yyyy/MM/dd"));
        assertEquals("01-06-2024 15:45", calc.format("dd-MM-yyyy HH:mm"));
        assertEquals("2024-06-01T15:45:30", calc.format("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Test
    void testIsBeforeOrEqualAndIsAfterOrEqual() {
        CalculatorTimestamp t1 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 10:00:00"));
        CalculatorTimestamp t2 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-01 10:00:00"));
        CalculatorTimestamp t3 = new CalculatorTimestamp(Timestamp.valueOf("2024-06-02 10:00:00"));
        assertTrue(t1.isBeforeOrEqual(t2));
        assertTrue(t1.isBeforeOrEqual(t3));
        assertFalse(t3.isBeforeOrEqual(t1));
        assertTrue(t3.isAfterOrEqual(t2));
        assertTrue(t2.isAfterOrEqual(t1));
        assertFalse(t1.isAfterOrEqual(t3));
    }
}
