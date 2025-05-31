package com.web.service;

public interface ClassEnrollmentService {
    void enrollClass(Long classId, Long memberId);
    void unenrollClass(Long classId, Long memberId);
}
