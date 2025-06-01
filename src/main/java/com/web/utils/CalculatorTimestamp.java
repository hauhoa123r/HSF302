package com.web.utils;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CalculatorTimestamp {
    private Timestamp timestamp;

    public CalculatorTimestamp() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public CalculatorTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public CalculatorTimestamp(Date date) {
        this.timestamp = new Timestamp(date.getTime());
    }

    public Timestamp plusDays(int days) {
        long milliseconds = timestamp.getTime() + (days * 24 * 60 * 60 * 1000L);
        return new Timestamp(milliseconds);
    }

    public Timestamp minusDays(int days) {
        long milliseconds = timestamp.getTime() - (days * 24 * 60 * 60 * 1000L);
        return new Timestamp(milliseconds);
    }

    public Timestamp plusHours(int hours) {
        long milliseconds = timestamp.getTime() + (hours * 60 * 60 * 1000L);
        return new Timestamp(milliseconds);
    }

    public Timestamp minusHours(int hours) {
        long milliseconds = timestamp.getTime() - (hours * 60 * 60 * 1000L);
        return new Timestamp(milliseconds);
    }

    public Timestamp plusMinutes(int minutes) {
        long milliseconds = timestamp.getTime() + (minutes * 60 * 1000L);
        return new Timestamp(milliseconds);
    }

    public Timestamp minusMinutes(int minutes) {
        long milliseconds = timestamp.getTime() - (minutes * 60 * 1000L);
        return new Timestamp(milliseconds);
    }

    public Timestamp plusSeconds(int seconds) {
        long milliseconds = timestamp.getTime() + (seconds * 1000L);
        return new Timestamp(milliseconds);
    }

    public Timestamp minusSeconds(int seconds) {
        long milliseconds = timestamp.getTime() - (seconds * 1000L);
        return new Timestamp(milliseconds);
    }

    public int getDay() {
        LocalDateTime ldt = toLocalDateTime();
        return ldt.getDayOfMonth();
    }

    public int getMonth() {
        LocalDateTime ldt = toLocalDateTime();
        return ldt.getMonthValue();
    }

    public int getYear() {
        LocalDateTime ldt = toLocalDateTime();
        return ldt.getYear();
    }

    public Timestamp getStartOfDay() {
        LocalDateTime ldt = toLocalDateTime().toLocalDate().atStartOfDay();
        return Timestamp.valueOf(ldt);
    }

    public Timestamp getEndOfDay() {
        LocalDate date = toLocalDateTime().toLocalDate();
        LocalDateTime endOfDay = date.atTime(23, 59, 59, 999_000_000);
        return Timestamp.valueOf(endOfDay);
    }

    public Date toSqlDate() {
        return new Date(timestamp.getTime());
    }

    public Timestamp getStartOfMonth() {
        LocalDateTime ldt = toLocalDateTime();
        LocalDateTime startOfMonth = ldt.withDayOfMonth(1).toLocalDate().atStartOfDay();
        return Timestamp.valueOf(startOfMonth);
    }

    public Timestamp getEndOfMonth() {
        LocalDateTime ldt = toLocalDateTime();
        LocalDate lastDay = ldt.toLocalDate().withDayOfMonth(ldt.toLocalDate().lengthOfMonth());
        LocalDateTime endOfMonth = lastDay.atTime(23, 59, 59, 999_000_000);
        return Timestamp.valueOf(endOfMonth);
    }

    public Timestamp getStartOfYear() {
        LocalDateTime ldt = toLocalDateTime();
        LocalDateTime startOfYear = ldt.withDayOfYear(1).toLocalDate().atStartOfDay();
        return Timestamp.valueOf(startOfYear);
    }

    public Timestamp getEndOfYear() {
        LocalDateTime ldt = toLocalDateTime();
        LocalDate lastDay = ldt.toLocalDate().withDayOfYear(ldt.toLocalDate().lengthOfYear());
        LocalDateTime endOfYear = lastDay.atTime(23, 59, 59, 999_000_000);
        return Timestamp.valueOf(endOfYear);
    }

    public LocalDateTime toLocalDateTime() {
        return timestamp.toLocalDateTime();
    }

    public boolean isBefore(CalculatorTimestamp other) {
        return this.timestamp.before(other.timestamp);
    }

    public boolean isAfter(CalculatorTimestamp other) {
        return this.timestamp.after(other.timestamp);
    }

    public boolean isSameDay(CalculatorTimestamp other) {
        LocalDate d1 = this.toLocalDateTime().toLocalDate();
        LocalDate d2 = other.toLocalDateTime().toLocalDate();
        return d1.isEqual(d2);
    }

    // Lấy thời điểm đầu tuần (Thứ Hai, 00:00:00) theo chuẩn ISO
    public Timestamp getStartOfWeek() {
        LocalDateTime ldt = toLocalDateTime();
        LocalDate monday = ldt.toLocalDate().with(java.time.temporal.WeekFields.ISO.dayOfWeek(), 1);
        LocalDateTime startOfWeek = monday.atStartOfDay();
        return Timestamp.valueOf(startOfWeek);
    }

    // Lấy thời điểm cuối tuần (Chủ Nhật, 23:59:59.999) theo chuẩn ISO
    public Timestamp getEndOfWeek() {
        LocalDateTime ldt = toLocalDateTime();
        LocalDate sunday = ldt.toLocalDate().with(java.time.temporal.WeekFields.ISO.dayOfWeek(), 7);
        LocalDateTime endOfWeek = sunday.atTime(23, 59, 59, 999_000_000);
        return Timestamp.valueOf(endOfWeek);
    }

    // Thứ tự trong tuần (1-7), mặc định 1: Thứ Hai, 7: Chủ Nhật (chuẩn ISO)
    public int getDayOfWeek() {
        LocalDateTime ldt = toLocalDateTime();
        return ldt.getDayOfWeek().getValue();
    }

    // Thứ tự ngày trong năm (1-365/366)
    public int getDayOfYear() {
        LocalDateTime ldt = toLocalDateTime();
        return ldt.getDayOfYear();
    }

    // Định dạng chuỗi ngày giờ theo pattern tuỳ ý
    public String format(String pattern) {
        LocalDateTime ldt = toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return ldt.format(formatter);
    }

    // So sánh trước hoặc bằng
    public boolean isBeforeOrEqual(CalculatorTimestamp other) {
        return !this.timestamp.after(other.timestamp);
    }

    // So sánh sau hoặc bằng
    public boolean isAfterOrEqual(CalculatorTimestamp other) {
        return !this.timestamp.before(other.timestamp);
    }
}