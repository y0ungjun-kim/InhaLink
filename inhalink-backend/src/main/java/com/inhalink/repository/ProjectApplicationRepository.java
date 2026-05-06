package com.inhalink.repository;

import com.inhalink.domain.ProjectApplication;
import com.inhalink.domain.ProjectPost;
import com.inhalink.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    // 나중에 특정 모집글(postId)에 지원한 목록을 다 보고 싶다면 기능 추가 가능
    // List<ProjectApplication> findByProjectPostId(Long postId);

    // 특정 사용자가 특정 게시글에 이미 지원했는지 여부를 boolean으로 반환 (스프링이 쿼리를 자동 생성)
    boolean existsByApplicantAndProjectPost(User applicant, ProjectPost projectPost);
}