package com.inhalink.service;

import com.inhalink.domain.User;
import com.inhalink.dto.request.UserProfileUpdateRequest;
import com.inhalink.repository.UserRepository;
import com.inhalink.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.inhalink.dto.request.SignupRequest;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignupRequest request) {
        // 1. 중복 체크
        validateDuplicateUser(request);
        // 2. 암호화 및 엔티티 생성
        User user = User.builder()
                .studentId(request.getStudentId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .contact(request.getContact())
                .build();
        // 3. 저장
        userRepository.save(user);
    }
       private void validateDuplicateUser(SignupRequest request) {
            if (userRepository.existsById(request.getStudentId())) {
                throw new IllegalArgumentException("이미 가입된 학번입니다.");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
        }

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