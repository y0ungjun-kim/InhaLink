package com.inhalink.dto.response;

import com.inhalink.domain.User;
import com.inhalink.domain.enums.Gender;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    private String name;
    private Gender gender;
    private String contact;
    private String domains;
    private String activities;

    // Entity를 받아서 바로 DTO로 변환하는 생성자 (아주 편리합니다!)
    public UserProfileResponse(User user) {
        this.name = user.getName();
        this.gender = user.getGender();
        this.contact = user.getContact();
        this.domains = user.getDomains();
        this.activities = user.getActivities();
    }
}