package com.inhalink.service;

import com.inhalink.domain.User;
import com.inhalink.dto.request.UserProfileUpdateRequest;
import com.inhalink.repository.UserRepository;
import com.inhalink.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User updateUserProfile(String studentId, UserProfileUpdateRequest request) {
        // 1. user 찾기
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new UserNotFoundException());

        // 2. 엔티티 내부의 부분 업데이트 메서드 호출
        user.updateProfile(
                request.getName(),
                request.getGender(),
                request.getContact(),
                request.getDomains(),
                request.getActivities()
        );

        // 3. 업데이트된 엔티티 반환 (컨트롤러에서 DTO로 변환하기 위함)
        return user;
    }
}