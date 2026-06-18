package com.inhalink.service;

import com.inhalink.domain.ProjectPost;
import com.inhalink.domain.User;
import com.inhalink.domain.enums.ActivityMethod;
import com.inhalink.domain.enums.PostCategory;
import com.inhalink.domain.enums.PostStatus;
import com.inhalink.dto.request.ProjectPostCreateRequest;
import com.inhalink.exception.UserNotFoundException;
import com.inhalink.repository.ProjectPostRepository;
import com.inhalink.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectPostServiceTest {

    @InjectMocks
    private ProjectPostService projectPostService;

    @Mock
    private ProjectPostRepository projectPostRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("정상적인 데이터로 모집글을 생성하면 저장된 게시글의 ID를 반환한다")
    void createPostSuccessTest() {
        // given
        String studentId = "12121212";
        LocalDateTime futureDeadline = LocalDateTime.now().plusDays(2);
        
        ProjectPostCreateRequest request = new ProjectPostCreateRequest();
        ReflectionTestUtils.setField(request, "title", "테스트 제목");
        ReflectionTestUtils.setField(request, "category", PostCategory.CONTEST);
        ReflectionTestUtils.setField(request, "projectName", "테스트 공모전");
        ReflectionTestUtils.setField(request, "content", "테스트 내용");
        ReflectionTestUtils.setField(request, "maxMembers", 4);
        ReflectionTestUtils.setField(request, "deadline", futureDeadline);
        ReflectionTestUtils.setField(request, "activityMethod", ActivityMethod.ONLINE);

        User writer = User.builder().studentId(studentId).build();
        ProjectPost post = ProjectPost.createNewPost(writer, "테스트 제목", PostCategory.CONTEST,
                "테스트 공모전", "테스트 내용", 4, futureDeadline, null, null, null, ActivityMethod.ONLINE);
        ReflectionTestUtils.setField(post, "id", 1L);

        given(userRepository.findById(studentId)).willReturn(Optional.of(writer));
        given(projectPostRepository.save(any(ProjectPost.class))).willReturn(post);

        // when
        Long savedId = projectPostService.createPost(studentId, request);

        // then
        assertThat(savedId).isEqualTo(1L);
        verify(projectPostRepository).save(any(ProjectPost.class));
    }

    @Test
    @DisplayName("마감 기한이 24시간 이내라면 예외가 발생한다")
    void createPostWithShortDeadlineTest() {
        // given
        String studentId = "12121212";
        ProjectPostCreateRequest request = new ProjectPostCreateRequest();
        ReflectionTestUtils.setField(request, "deadline", LocalDateTime.now().plusHours(5));

        // when & then
        assertThatThrownBy(() -> projectPostService.createPost(studentId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("24시간 이후");
    }

    @Test
    @DisplayName("존재하지 않는 유저 학번으로 글을 작성하려 하면 예외가 발생한다")
    void createPostUserNotFoundTest() {
        // given
        String studentId = "99999999";
        ProjectPostCreateRequest request = new ProjectPostCreateRequest();
        ReflectionTestUtils.setField(request, "deadline", LocalDateTime.now().plusDays(2));

        given(userRepository.findById(studentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> projectPostService.createPost(studentId, request))
                .isInstanceOf(UserNotFoundException.class);
    }
}
