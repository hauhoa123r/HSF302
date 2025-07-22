package com.web.api;

import com.web.converter.OtpStorage;
import com.web.model.dto.*;
import com.web.service.EmailService;
import com.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/user")
public class UserAPI {

    @Autowired
    UserService userSericeImpl;

    @Autowired
    private EmailService emailService;


    @Autowired
    private OtpStorage otpStorage;

    @PostMapping("/login")
    public ResponseEntity<?> isLogin(@RequestBody UserLoginDTO userLoginDTO) {
        Boolean userLoginResponse = userSericeImpl.isLogin(userLoginDTO);
        if (userLoginResponse) {
            return ResponseEntity.ok(userLoginResponse);
        }
        return ResponseEntity.badRequest().body(userLoginResponse);
    }

    @GetMapping("/verified/email/{email}")
    public ResponseEntity<?> isVerifiedEmail(@PathVariable String email) {
        Boolean isVerifiedEmail = userSericeImpl.isVerifiedEmail(email);
        if (!isVerifiedEmail) {
            return ResponseEntity.ok(isVerifiedEmail);
        }
        return ResponseEntity.badRequest().body("failed");
    }

    @GetMapping("/verified/phone/{phone}")
    public ResponseEntity<?> isVerifiedPhone(@PathVariable String phone) {
        Boolean isVerifiedPhone = userSericeImpl.isVerifiedPhone(phone);
        if (!isVerifiedPhone) {
            return ResponseEntity.ok(isVerifiedPhone);
        }
        return ResponseEntity.badRequest().body("failed");
    }

    @GetMapping("/verified/username/{username}")
    public ResponseEntity<?> isVerifiedUsername(@PathVariable String username) {
        Boolean isVerifiedUsername = userSericeImpl.isVerifiedUsername(username);
        if (!isVerifiedUsername) {
            return ResponseEntity.ok(isVerifiedUsername);
        }
        return ResponseEntity.badRequest().body("failed");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) throws ParseException {
        Boolean isRegister = userSericeImpl.isRegister(userRegisterDTO);
        if (isRegister) {
            return ResponseEntity.ok(isRegister);
        }
        return ResponseEntity.badRequest().body("failed");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOTP(@RequestBody OTPDTO otpDTO) {
        String otp = emailService.sendEmail(otpDTO.getEmail());
        otpStorage.saveOtp(otpDTO.getEmail(), otp);
        return ResponseEntity.ok("Đã gửi mã OTP đến " + otpDTO.getEmail());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestBody OTPVerifyDTO dto) {
        boolean valid = otpStorage.verifyOtp(dto.getEmail(), dto.getOtp());
        if (valid) {
            return ResponseEntity.ok("Mã OTP hợp lệ");
        } else {
            return ResponseEntity.badRequest().body("Mã OTP không hợp lệ hoặc đã hết hạn");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        Boolean isResetPassword = userSericeImpl.isResetPassword(resetPasswordDTO);
        if (isResetPassword) {
            return ResponseEntity.ok(isResetPassword);
        }
        return ResponseEntity.badRequest().body("failed");
    }
}