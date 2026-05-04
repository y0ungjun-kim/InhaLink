package com.inhalink.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@RedisHash(value = "emailVerification", timeToLive = 300) // 5분 후 자동 삭제
public class EmailVerification {

    @Id
    private String email; // 이메일 (PK 역할)

    private String verificationCode; // 인증코드

    private LocalDateTime createdAt; // 생성시간

    public EmailVerification(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.createdAt = LocalDateTime.now();
    }
}