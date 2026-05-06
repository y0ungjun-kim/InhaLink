package com.inhalink.domain;

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
    private String studentId; // 학번 (PK)

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String gender;

    @Column(nullable = false, length = 20)
    private String contact;

    @Column(length = 255)
    private String domains; // 관심 분야

    @Column(columnDefinition = "TEXT")
    private String activities; // 대외활동 이력

    public void updateProfile(String name, String gender, String contact, String domains, String activities) {
        // 넘어온 값이 비어있지 않을 때만 기존 값 대체
        if (name != null && !name.isBlank()) this.name = name;
        if (gender != null && !gender.isBlank()) this.gender = gender;
        if (contact != null && !contact.isBlank()) this.contact = contact;

        // 이 두 개는 선택값이므로 빈 문자열이 들어와도 처리
        if (domains != null) this.domains = domains;
        if (activities != null) this.activities = activities;
    }
}
