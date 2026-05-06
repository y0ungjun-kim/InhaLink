package com.inhalink.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.inhalink.domain.enums.PostStatus;

@Entity
@Table(name = "project_posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectPost extends BaseTimeEntity {

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

    @Enumerated(EnumType.STRING) // DB에 숫자가 아닌 문자열로 저장하도록 설정
    @Column(nullable = false, length = 20)
    private PostStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private ProjectPost(User writer, String title, String content, LocalDateTime deadline, PostStatus status) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.status = status;
    }

    // 정적 팩토리 메서드 도입 (생성과 동시에 초기 상태를 스스로 세팅)
    public static ProjectPost createNewPost(User writer, String title, String content, LocalDateTime deadline) {
        return ProjectPost.builder()
                .writer(writer)
                .title(title)
                .content(content)
                .deadline(deadline)
                .status(PostStatus.RECRUITING)
                .build();
    }

    public void close() {
        if (this.status == PostStatus.CLOSED) {
            throw new IllegalStateException("이미 마감된 게시글입니다.");
        }
        this.status = PostStatus.CLOSED;
    }
}