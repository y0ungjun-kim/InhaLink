package com.inhalink.service;

import com.inhalink.domain.EmailVerification;
import com.inhalink.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    private static final String DOMAIN_AC_KR = "@inha.ac.kr";
    private static final String DOMAIN_EDU = "@inha.edu";

    @Transactional
    public void sendVerificationCode(String email) {
        // 1. 학교 이메일인지 확인 (ac.kr 또는 edu)
        if (!email.endsWith(DOMAIN_AC_KR) && !email.endsWith(DOMAIN_EDU)) {
            throw new IllegalArgumentException("인하대학교 이메일(@inha.ac.kr 또는 @inha.edu)만 사용 가능합니다.");
        }

        // 2. 인증코드 생성 (6자리 난수)
        String verificationCode = generateCode();

        // 3. 이메일 발송
        sendEmail(email, verificationCode);

        // 4. Redis에 저장 (이미 EmailVerification에 TTL 5분이 설정되어 있음)
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[InhaLink] 이메일 인증 번호");
        message.setText("인증 번호는 [" + code + "] 입니다. 5분 이내에 입력해주세요.");
        mailSender.send(message);
    }
}
