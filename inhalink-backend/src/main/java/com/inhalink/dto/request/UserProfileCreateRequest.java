package com.inhalink.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileCreateRequest {

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    @NotBlank(message = "성별은 필수 입력 사항입니다.")
    private String gender;

    @NotBlank(message = "연락처는 필수 입력 사항입니다.")
    private String contact;

    @NotBlank(message = "학과는 필수 입력 사항입니다.")
    private String department;

    @NotBlank(message = "관심 분야는 필수 입력 사항입니다.")
    private String domains;

    // 대외활동은 선택
    private String activities;
}
