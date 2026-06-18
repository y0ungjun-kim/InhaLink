package com.inhalink.service;

import com.inhalink.domain.User;
import com.inhalink.dto.request.UserProfileUpdateRequest;
import com.inhalink.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 프로필 업데이트 시 엔티티의 정보가 변경되어야 한다")
    void updateUserProfileTest() {
        // given
        String studentId = "12121212";
        User user = User.builder()
                .studentId(studentId)
                .name("기존이름")
                .gender("M")
                .contact("010-1111-1111")
                .build();

        UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        ReflectionTestUtils.setField(request, "name", "새이름");
        ReflectionTestUtils.setField(request, "gender", "F");
        ReflectionTestUtils.setField(request, "contact", "010-2222-2222");
        ReflectionTestUtils.setField(request, "domains", "백엔드");
        ReflectionTestUtils.setField(request, "activities", "대외활동");

        given(userRepository.findById(studentId)).willReturn(Optional.of(user));

        // when
        User updatedUser = userService.updateUserProfile(studentId, request);

        // then
        assertThat(updatedUser.getName()).isEqualTo("새이름");
        assertThat(updatedUser.getGender()).isEqualTo("F");
        assertThat(updatedUser.getContact()).isEqualTo("010-2222-2222");
        assertThat(updatedUser.getDomains()).isEqualTo("백엔드");
    }
}
