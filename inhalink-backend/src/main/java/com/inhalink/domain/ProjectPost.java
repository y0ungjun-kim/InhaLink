package com.inhalink.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 모집글 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_student_id")
    private User writer; // 작성자 (FK)

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false, length = 20)
    private String status; // RECRUITING, CLOSED 등
}