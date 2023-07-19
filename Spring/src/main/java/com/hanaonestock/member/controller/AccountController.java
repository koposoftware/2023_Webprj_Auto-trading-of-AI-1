package com.hanaonestock.member.controller;

import com.hanaonestock.member.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController {

    private final EmailService emailService;

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody String email) {
        try {
            String decodedEmail = URLDecoder.decode(email, StandardCharsets.UTF_8);
            String emailAddress = decodedEmail.replace("email=", "");
            System.out.println(emailAddress);
            String confirmationCode = emailService.sendSimpleMessage(emailAddress);
            return confirmationCode;
        } catch (Exception e) {
            log.error("이메일 전송 중 오류 발생", e);
            return "오류가 발생했습니다. 다시 시도해주세요.";
        }
    }
}