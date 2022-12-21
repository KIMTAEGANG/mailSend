package com.example.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSenderImpl mailSender;

    public String mailAuth(String email) {
        String authCode = makeAuthCode();
        MimeMessage message = mailSender.createMimeMessage();
        String from = sender;
        String to = email;
        String title = "회원 가입 인증 이메일";
        String context = "인증 번호는 " + authCode + "입니다." + "<br/><br/>"
                + "인증번호를 입력해 확인란에 기입하여 주세요";
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(context, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return authCode;
    }

    private String makeAuthCode() {
        SecureRandom random = new SecureRandom();
        String code = "";
        for(int i=0; i< 6; i++) {
            code += random.nextInt(10);
        }
        return code;
    }
}
