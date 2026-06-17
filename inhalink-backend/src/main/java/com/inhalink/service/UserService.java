package com.inhalink.service;

import com.inhalink.domain.User;
import com.inhalink.dto.request.UserProfileCreateRequest;
import com.inhalink.dto.request.UserProfileUpdateRequest;
import com.inhalink.exception.UserNotFoundException;
import com.inhalink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 로그인 후 최초 1회 프로필 작성 (모든 필수 항목이 채워져야 완료 처리)
    @Transactional
    public User createProfile(String studentId, UserProfileCreateRequest request) {
        User user = userRepository.findById(studentId)
                .orElseThrow(UserNotFoundException::new);

        if (user.isProfileComplete()) {
            throw new IllegalStateException("이미 프로필을 작성하셨습니다. 수정은 마이페이지에서 이용해 주세요.");
        }

        user.createProfile(
                request.getName(),
                request.getGender(),
                request.getContact(),
                request.getDepartment(),
                request.getDomains(),
                request.getActivities()
        );

        return user;
    }

    // 마이페이지에서 프로필 수정 (부분 업데이트)
    @Transactional
    public User updateUserProfile(String studentId, UserProfileUpdateRequest request) {
        User user = userRepository.findById(studentId)
                .orElseThrow(UserNotFoundException::new);

        user.updateProfile(
                request.getName(),
                request.getGender(),
                request.getContact(),
                request.getDepartment(),
                request.getDomains(),
                request.getActivities()
        );

        return user;
    }

    @Transactional(readOnly = true)
    public User getProfile(String studentId) {
        return userRepository.findById(studentId)
                .orElseThrow(UserNotFoundException::new);
    }
}