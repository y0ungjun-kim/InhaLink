package com.inhalink.service;

import com.inhalink.domain.ProjectPost;
import com.inhalink.domain.User;
import com.inhalink.domain.enums.PostStatus;
import com.inhalink.dto.request.ProjectPostCreateRequest;
import com.inhalink.exception.UserNotFoundException;
import com.inhalink.repository.ProjectPostRepository;
import com.inhalink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createPost(String studentId, ProjectPostCreateRequest request) {
        // 검증: 생성하자마자 마감되는 "낚시글"을 방지를 위해 현재 시간보다 최소 1시간 이후인지 체크
        if (request.getDeadline().isBefore(LocalDateTime.now().plusDays(1))) {
            throw new IllegalArgumentException("마감 기한은 현재 시간으로부터 최소 24시간 이후여야 합니다.");
        }

        // 1. 작성자 찾기
        User writer = userRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException());

        // 2. 정적 팩토리 메서드를 이용한 엔티티 생성
        ProjectPost post = ProjectPost.createNewPost(
                writer,
                request.getTitle(),
                request.getContent(),
                request.getDeadline()
        );

        // 3. DB 저장 후 생성된 ID 반환
        ProjectPost savedPost = projectPostRepository.save(post);
        return savedPost.getId();
    }
}