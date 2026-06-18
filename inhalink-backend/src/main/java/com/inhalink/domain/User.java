package com.inhalink.domain;

import com.inhalink.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(nullable = false, length = 20)
    private String contact;

    @Column(length = 100)
    private String department; // 학과

    @Column(length = 255)
    private String domains; // 관심 분야

    @Column(columnDefinition = "TEXT")
    private String activities; // 대외활동 이력

    @Column(nullable = false)
    @Builder.Default
    private boolean profileComplete = false; // 최초 프로필 작성 완료 여부

    // 최초 프로필 작성: 필수 항목을 모두 채우고 완료 처리
    public void createProfile(String name, Gender gender, String contact, String department,
                              String domains, String activities) {
        this.name = name;
        this.gender = gender;
        this.contact = contact;
        this.department = department;
        this.domains = domains;
        this.activities = activities;
        this.profileComplete = true;
    }

    // 마이페이지 수정: 넘어온 값이 있을 때만 교체 (부분 업데이트)
    public void updateProfile(String name, Gender gender, String contact, String department,
                              String domains, String activities) {
        if (name != null && !name.isBlank()) this.name = name;
        if (gender != null) this.gender = gender;
        if (contact != null && !contact.isBlank()) this.contact = contact;
        if (department != null && !department.isBlank()) this.department = department;
        if (domains != null) this.domains = domains;
        if (activities != null) this.activities = activities;
    }
}
