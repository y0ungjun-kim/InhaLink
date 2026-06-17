package com.inhalink.service;

import com.inhalink.domain.ProjectApplication;
import com.inhalink.domain.ProjectPost;
import com.inhalink.domain.User;
import com.inhalink.domain.enums.ActivityMethod;
import com.inhalink.domain.enums.ApplicationStatus;
import com.inhalink.domain.enums.PostCategory;
import com.inhalink.domain.enums.PostStatus;
import com.inhalink.exception.PostNotFoundException;
import com.inhalink.exception.UserNotFoundException;
import com.inhalink.repository.ProjectApplicationRepository;
import com.inhalink.repository.ProjectPostRepository;
import com.inhalink.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectApplicationServiceTest {

    @InjectMocks
    private ProjectApplicationService projectApplicationService;

    @Mock
    private ProjectApplicationRepository applicationRepository;

    @Mock
    private ProjectPostRepository projectPostRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("정상적인 지원 요청 시 지원서 ID를 반환한다")
    void applyForProjectSuccessTest() {
        // given
        String applicantId = "applicant123";
        Long postId = 1L;

        User applicant = User.builder().studentId(applicantId).build();
        User writer = User.builder().studentId("writer456").build();
        ProjectPost post = ProjectPost.createNewPost(writer, "제목", PostCategory.CONTEST, "공모전명", "내용", 4, LocalDateTime.now().plusDays(2), null, null, null, ActivityMethod.ONLINE);

        ProjectApplication application = ProjectApplication.applyToPost(applicant, post);
        ReflectionTestUtils.setField(application, "id", 100L);

        given(userRepository.findById(applicantId)).willReturn(Optional.of(applicant));
        given(projectPostRepository.findById(postId)).willReturn(Optional.of(post));
        given(applicationRepository.existsByApplicantAndProjectPost(applicant, post)).willReturn(false);
        given(applicationRepository.save(any(ProjectApplication.class))).willReturn(application);

        // when
        Long savedId = projectApplicationService.applyForProject(applicantId, postId);

        // then
        assertThat(savedId).isEqualTo(100L);
    }

    @Test
    @DisplayName("본인의 게시글에 지원하려 하면 예외가 발생한다")
    void applyToSelfPostTest() {
        // given
        String studentId = "user123";
        User user = User.builder().studentId(studentId).build();
        ProjectPost post = ProjectPost.createNewPost(user, "제목", PostCategory.CONTEST, "공모전명", "내용", 4, LocalDateTime.now().plusDays(2), null, null, null, ActivityMethod.ONLINE);

        given(userRepository.findById(studentId)).willReturn(Optional.of(user));
        given(projectPostRepository.findById(1L)).willReturn(Optional.of(post));

        // when & then
        assertThatThrownBy(() -> projectApplicationService.applyForProject(studentId, 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("본인이 작성한");
    }

    @Test
    @DisplayName("이미 마감된 게시글에 지원하려 하면 예외가 발생한다")
    void applyToClosedPostTest() {
        // given
        User applicant = User.builder().studentId("applicant").build();
        User writer = User.builder().studentId("writer").build();
        ProjectPost post = ProjectPost.createNewPost(writer, "제목", PostCategory.CONTEST, "공모전명", "내용", 4, LocalDateTime.now().plusDays(2), null, null, null, ActivityMethod.ONLINE);
        post.close(); // 마감 처리

        given(userRepository.findById("applicant")).willReturn(Optional.of(applicant));
        given(projectPostRepository.findById(1L)).willReturn(Optional.of(post));

        // when & then
        assertThatThrownBy(() -> projectApplicationService.applyForProject("applicant", 1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 마감된");
    }

    @Test
    @DisplayName("게시글 작성자가 아닌 유저가 지원 상태를 변경하려 하면 예외가 발생한다")
    void updateStatusForbiddenTest() {
        // given
        String otherUserId = "otherUser";
        User writer = User.builder().studentId("writer").build();
        ProjectPost post = ProjectPost.createNewPost(writer, "제목", PostCategory.CONTEST, "공모전명", "내용", 4, LocalDateTime.now().plusDays(2), null, null, null, ActivityMethod.ONLINE);
        ProjectApplication application = ProjectApplication.applyToPost(null, post);

        given(applicationRepository.findById(1L)).willReturn(Optional.of(application));

        // when & then
        assertThatThrownBy(() -> projectApplicationService.updateApplicationStatus(otherUserId, 1L, ApplicationStatus.ACCEPTED))
                .isInstanceOf(AccessDeniedException.class);
    }
}
