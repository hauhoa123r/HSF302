package com.web.converter;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStorage {

    private final Map<String, OtpEntry> otpMap = new ConcurrentHashMap<>();

    public void saveOtp(String email, String otp) {
        otpMap.put(email, new OtpEntry(otp, LocalDateTime.now().plusMinutes(10)));
    }

    public boolean verifyOtp(String email, String inputOtp) {
        OtpEntry entry = otpMap.get(email);
        if (entry == null) return false;
        if (entry.expiresAt.isBefore(LocalDateTime.now())) {
            otpMap.remove(email);
            return false;
        }
        boolean match = entry.otp.equals(inputOtp);
        if (match) otpMap.remove(email);
        return match;
    }

    private static class OtpEntry {
        String otp;
        LocalDateTime expiresAt;

        OtpEntry(String otp, LocalDateTime expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }
}
