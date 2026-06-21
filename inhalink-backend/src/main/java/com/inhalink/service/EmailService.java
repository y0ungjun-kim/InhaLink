package com.inhalink.service;

import com.inhalink.domain.EmailVerification;
import com.inhalink.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final EmailVerificationRepository emailVerificationRepository;
    private final RestTemplate restTemplate;

    @Value("${brevo.api-key}")
    private String brevoApiKey;

    private static final String DOMAIN_AC_KR = "@inha.ac.kr";
    private static final String DOMAIN_EDU = "@inha.edu";

    @Transactional
    public void sendVerificationCode(String email) {
        if (!email.endsWith(DOMAIN_AC_KR) && !email.endsWith(DOMAIN_EDU)) {
            throw new IllegalArgumentException("인하대학교 이메일(@inha.ac.kr 또는 @inha.edu)만 사용 가능합니다.");
        }

        String verificationCode = generateCode();
        sendEmail(email, verificationCode);

        EmailVerification verification = new EmailVerification(email, verificationCode);
        emailVerificationRepository.save(verification);
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        return emailVerificationRepository.findById(email)
                .map(verification -> {
                    if (verification.getVerificationCode().equals(code)) {
                        verification.verify();
                        emailVerificationRepository.save(verification);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    public boolean isVerified(String email) {
        return emailVerificationRepository.findById(email)
                .map(EmailVerification::isVerified)
                .orElse(false);
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    private void sendEmail(String to, String code) {
        log.info("BREVO_API_KEY length: {}, starts with: {}", brevoApiKey.length(), brevoApiKey.substring(0, Math.min(10, brevoApiKey.length())));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", "InhaLink", "email", "admininhalink@gmail.com"),
                "to", List.of(Map.of("email", to)),
                "subject", "[InhaLink] 이메일 인증 번호",
                "textContent", "인증 번호는 [" + code + "] 입니다. 5분 이내에 입력해주세요."
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity("https://api.brevo.com/v3/smtp/email", request, String.class);
            log.info("Brevo response: {}", response.getStatusCode());
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Brevo error status: {}, body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }
}
