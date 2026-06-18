package com.inhalink.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    private String password;
}
