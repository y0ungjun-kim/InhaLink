package com.inhalink.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 마이페이지 수정용 - 보낸 필드만 업데이트 (부분 업데이트)
@Getter
@NoArgsConstructor
public class UserProfileUpdateRequest {

    private String name;
    private String gender;
    private String contact;
    private String department;
    private String domains;
    private String activities;
}