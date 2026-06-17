package com.inhalink.domain;

import com.inhalink.domain.enums.ActivityMethod;
import com.inhalink.domain.enums.PostCategory;
import com.inhalink.domain.enums.PostStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectPostTest {

    private ProjectPost buildSamplePost(User writer) {
        return ProjectPost.createNewPost(
                writer, "테스트 제목", PostCategory.CONTEST, "테스트 공모전",
                "테스트 내용", 4, LocalDateTime.now().plusDays(2),
                null, null, null, ActivityMethod.ONLINE
        );
    }

    @Test
    @DisplayName("정적 팩토리 메서드로 게시글 생성 시 초기 상태는 RECRUITING이어야 한다")
    void createNewPostTest() {
        User writer = User.builder().studentId("12121212").name("홍길동").build();

        ProjectPost post = buildSamplePost(writer);

        assertThat(post.getStatus()).isEqualTo(PostStatus.RECRUITING);
        assertThat(post.getTitle()).isEqualTo("테스트 제목");
        assertThat(post.getWriter()).isEqualTo(writer);
    }

    @Test
    @DisplayName("이미 마감된 게시글을 다시 마감하려고 하면 예외가 발생한다")
    void closeAlreadyClosedPostTest() {
        ProjectPost post = buildSamplePost(null);
        post.close();

        assertThatThrownBy(() -> post.close())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 마감된 게시글입니다.");
    }
}
