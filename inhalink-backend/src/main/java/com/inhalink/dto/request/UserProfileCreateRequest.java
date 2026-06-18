package com.inhalink.dto.request;

import com.inhalink.domain.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileCreateRequest {

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    @NotNull(message = "성별은 필수 입력 사항입니다.")
    private Gender gender;

    @NotBlank(message = "연락처는 필수 입력 사항입니다.")
    private String contact;

    @NotBlank(message = "학과는 필수 입력 사항입니다.")
    private String department;

    @NotBlank(message = "관심 분야는 필수 입력 사항입니다.")
    private String domains;

    private String activities;
}
