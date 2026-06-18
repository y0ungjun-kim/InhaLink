package com.inhalink.dto.request;

import com.inhalink.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "학번은 필수입니다.")
    private String studentId;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @NotBlank(message = "연락처는 필수입니다.")
    private String contact;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getStudentId() {
        return studentId;
    }

    public Gender getGender() {
        return gender;
    }

    public String getContact() {
        return contact;
    }
}

