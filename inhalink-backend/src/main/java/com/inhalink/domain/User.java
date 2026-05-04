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
}
