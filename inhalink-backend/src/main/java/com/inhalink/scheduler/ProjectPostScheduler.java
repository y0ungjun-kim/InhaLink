package com.inhalink.scheduler;

import com.inhalink.domain.ProjectPost;
import com.inhalink.domain.enums.PostStatus;
import com.inhalink.repository.ProjectPostRepository;
import lombok.extern.slf4j.Slf4j; // 별도의 코드 없이 log 객체 자동 주입을 위해 사용
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectPostScheduler {

    private final ProjectPostRepository projectPostRepository;

    // 매 10분마다 실행되도록 설정 (cron 표현식: "초 분 시 일 월 요일")
    // 만약 매분마다 돌리고 싶다면 "0 * * * * *" 로 변경
    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void autoCloseExpiredPosts() {
        // 1. 마감일이 지난 모집글들을 DB에서 추출
        List<ProjectPost> expiredPosts = projectPostRepository.findExpiredPosts(PostStatus.RECRUITING);

        // 2. 꺼내온 게시글들을 하나씩 순회하며 엔티티 로직인 close()를 호출
        for (ProjectPost post : expiredPosts) {
            post.close(); // JPA 더티 체킹 덕분에 자동으로 DB에 UPDATE 쿼리 날아감
        }

        if(!expiredPosts.isEmpty()) {
            log.info("마감기한이 지난 모집글 {}건이 비활성화(CLOSED) 되었습니다.", expiredPosts.size());
        }
    }
}