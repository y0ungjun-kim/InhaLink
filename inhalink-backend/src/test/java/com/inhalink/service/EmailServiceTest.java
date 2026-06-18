package com.inhalink.service;

import com.inhalink.domain.EmailVerification;
import com.inhalink.repository.EmailVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Test
    @DisplayName("인하대 이메일이 아니면 예외가 발생한다")
    void sendVerificationCode_Fail_InvalidDomain() {
        // given
        String email = "test@gmail.com";

        // when & then
        assertThatThrownBy(() -> emailService.sendVerificationCode(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("인하대학교 이메일(@inha.ac.kr 또는 @inha.edu)만 사용 가능합니다.");
    }

    @Test
    @DisplayName("인하대 이메일(@inha.ac.kr)이면 인증번호를 생성하고 메일을 발송한다")
    void sendVerificationCode_Success_AcKr() {
        // given
        String email = "test@inha.ac.kr";

        // when
        emailService.sendVerificationCode(email);

        // then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailVerificationRepository, times(1)).save(any(EmailVerification.class));
    }

    @Test
    @DisplayName("인하대 이메일(@inha.edu)이면 인증번호를 생성하고 메일을 발송한다")
    void sendVerificationCode_Success_Edu() {
        // given
        String email = "test@inha.edu";

        // when
        emailService.sendVerificationCode(email);

        // then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailVerificationRepository, times(1)).save(any(EmailVerification.class));
    }

    @Test
    @DisplayName("올바른 인증번호를 입력하면 true를 반환한다")
    void verifyCode_Success() {
        // given
        String email = "test@inha.ac.kr";
        String code = "123456";
        EmailVerification verification = new EmailVerification(email, code);
        given(emailVerificationRepository.findById(email)).willReturn(Optional.of(verification));

        // when
        boolean result = emailService.verifyCode(email, code);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("틀린 인증번호를 입력하면 false를 반환한다")
    void verifyCode_Fail() {
        // given
        String email = "test@inha.ac.kr";
        String code = "123456";
        String wrongCode = "000000";
        EmailVerification verification = new EmailVerification(email, code);
        given(emailVerificationRepository.findById(email)).willReturn(Optional.of(verification));

        // when
        boolean result = emailService.verifyCode(email, wrongCode);

        // then
        assertThat(result).isFalse();
    }
}
