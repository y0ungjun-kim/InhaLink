package com.inhalink.controller;

import com.inhalink.dto.request.EmailSendRequest;
import com.inhalink.dto.request.EmailVerifyRequest;
import com.inhalink.dto.response.ApiResponse;
import com.inhalink.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Email API", description = "이메일 인증 관련 API")
@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "인증번호 발송", description = "입력한 이메일로 6자리 인증번호를 발송합니다.")
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(@Valid @RequestBody EmailSendRequest request) {
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success("인증번호가 발송되었습니다.", null));
    }

    @Operation(summary = "인증번호 검증", description = "사용자가 입력한 인증번호를 확인합니다.")
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(@Valid @RequestBody EmailVerifyRequest request) {
        boolean isVerified = emailService.verifyCode(request.getEmail(), request.getVerificationCode());
        if (isVerified) {
            return ResponseEntity.ok(ApiResponse.success("인증에 성공하였습니다.", true));
        } else {
            return ResponseEntity.ok(ApiResponse.success("인증번호가 일치하지 않거나 만료되었습니다.", false));
        }
    }
}
