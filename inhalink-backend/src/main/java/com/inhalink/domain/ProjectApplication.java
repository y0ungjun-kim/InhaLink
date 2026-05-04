package com.inhalink.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_applications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 지원 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_student_id")
    private User applicant; // 지원자 (FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private ProjectPost projectPost; // 지원한 글 (FK)

    @Column(nullable = false, length = 20)
    private String status; // PENDING, ACCEPTED, REJECTED

    // 나중에 속성 추가 시 이 자리에 작성
    // private String githubUrl;
    // private String portfolioUrl;
}