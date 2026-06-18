package com.inhalink.dto.response;

import com.inhalink.domain.User;
import com.inhalink.domain.enums.Gender;
import lombok.Getter;

@Getter
public class UserProfileResponse {

    private String studentId;
    private String name;
    private Gender gender;
    private String contact;
    private String department;
    private String domains;
    private String activities;
    private boolean profileComplete;

    public UserProfileResponse(User user) {
        this.studentId = user.getStudentId();
        this.name = user.getName();
        this.gender = user.getGender();
        this.contact = user.getContact();
        this.department = user.getDepartment();
        this.domains = user.getDomains();
        this.activities = user.getActivities();
        this.profileComplete = user.isProfileComplete();
    }
}