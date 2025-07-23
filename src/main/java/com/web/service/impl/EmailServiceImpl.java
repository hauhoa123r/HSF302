package com.web.service.impl;

import com.web.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String sendEmail(String toEmail) {
        String otp = generateOtp();
        String htmlTemplate = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>GymFitness OTP Verification</title>
                    <style>
                        body {
                            margin: 0;
                            padding: 0;
                            background-color: #1a1a1a;
                            font-family: Arial, sans-serif;
                            color: #ffffff;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #2d2d2d;
                            padding: 20px;
                            border-radius: 10px;
                            box-shadow: 0 0 15px rgba(255, 165, 0, 0.5);
                        }
                        .header {
                            text-align: center;
                            padding: 20px 0;
                            background-color: #f97316;
                            border-radius: 10px 10px 0 0;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 28px;
                            color: #1a1a1a;
                        }
                        .content {
                            padding: 20px;
                            text-align: center;
                        }
                        .otp-code {
                            font-size: 36px;
                            font-weight: bold;
                            color: #f97316;
                            letter-spacing: 5px;
                            margin: 20px 0;
                            padding: 10px;
                            background-color: #3d3d3d;
                            border-radius: 5px;
                            display: inline-block;
                        }
                        .content p {
                            font-size: 16px;
                            line-height: 1.5;
                            color: #d1d5db;
                        }
                        .footer {
                            text-align: center;
                            padding: 20px;
                            font-size: 14px;
                            color: #9ca3af;
                        }
                        .footer a {
                            color: #f97316;
                            text-decoration: none;
                        }
                        .footer a:hover {
                            text-decoration: underline;
                        }
                    </style>
                </head>
                <body>
                           <div class="container">
                               <div class="header">
                                   <h1>GymFitness</h1>
                               </div>
                               <div class="content">
                                   <h2>Mã xác thực OTP</h2>
                                   <p>Cảm ơn bạn đã lựa chọn GymFitness! Vui lòng sử dụng mã OTP dưới đây để xác minh tài khoản của bạn:</p>
                                   <div class="otp-code">%s</div>
                                   <p>Mã OTP này có hiệu lực trong vòng 10 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.</p>
                                   <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email hoặc liên hệ với bộ phận hỗ trợ của chúng tôi.</p>
                               </div>
                               <div class="footer">
                                   <p>&copy; 2025 GymFitness. Mọi quyền được bảo lưu.</p>
                                   <p><a href="http://localhost:9090/login">Truy cập website</a> | <a href="mailto:hoahvhe186810@fpt.edu.vn">Liên hệ hỗ trợ</a></p>
                               </div>
                           </div>
                </body>
                </html>
                """.formatted(otp);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Mã OTP xác thực - GymFitness");
            helper.setFrom("hoahvhe186810@fpt.edu.vn");
            helper.setText(htmlTemplate, true);
            mailSender.send(message);
            return otp;
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email", e);
        }
    }


    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
