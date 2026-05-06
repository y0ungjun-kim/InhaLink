package com.inhalink.service;

import com.inhalink.domain.ProjectApplication;
import com.inhalink.domain.ProjectPost;
import com.inhalink.domain.User;
import com.inhalink.domain.enums.ApplicationStatus;
import com.inhalink.domain.enums.PostStatus;
import com.inhalink.exception.PostNotFoundException;
import com.inhalink.exception.UserNotFoundException;
import com.inhalink.repository.ProjectApplicationRepository;
import com.inhalink.repository.ProjectPostRepository;
import com.inhalink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectApplicationService {

    private final ProjectApplicationRepository applicationRepository;
    private final ProjectPostRepository projectPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long applyForProject(String applicantId, Long postId) {
        // 1. 지원자와 모집글 DB에서 찾기
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new UserNotFoundException());
        ProjectPost post = projectPostRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        // 검증 1: 마감된 글 지원 금지
        if (post.getStatus() == PostStatus.CLOSED) {
            throw new IllegalStateException("이미 마감된 모집글에는 지원할 수 없습니다.");
        }

        // 검증 2: 자기 자신 지원 금지 (작성자의 학번과 지원자의 학번 비교)
        if (post.getWriter().getStudentId().equals(applicantId)) {
            throw new IllegalStateException("본인이 작성한 모집글에는 지원할 수 없습니다.");
        }

        // 검증 3: 중복 지원 금지 (Repository에 만들어둔 메서드 활용)
        if (applicationRepository.existsByApplicantAndProjectPost(applicant, post)) {
            throw new IllegalStateException("이미 해당 모집글에 지원하셨습니다.");
        }

        // 3. 위 조건들을 모두 통과하면 지원서 객체 생성 (PENDING 상태 자동 적용)
        ProjectApplication application = ProjectApplication.applyToPost(applicant, post);

        // 4. DB에 저장하고 생성된 ID 반환
        return applicationRepository.save(application).getId();
    }

    // 지원서를 수락하거나 거절할 때, 요청을 보낸 사람이 실제 글쓴이인지 확인
    @Transactional
    public void updateApplicationStatus(String loginId, Long applicationId, ApplicationStatus newStatus) {
        ProjectApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new PostNotFoundException());

        // 이 게시글의 작성자가 현재 로그인한 유저가 맞는지 확인
        String writerId = application.getProjectPost().getWriter().getStudentId();
        if (!writerId.equals(loginId)) {
            // Spring Security의 예외를 사용해 403 응답이 나가도록 함
            throw new org.springframework.security.access.AccessDeniedException("해당 지원서를 처리할 권한이 없습니다.");
        }

        if (newStatus == ApplicationStatus.ACCEPTED) {
            application.accept();
        } else if (newStatus == ApplicationStatus.REJECTED) {
            application.reject();
        }
    }
}