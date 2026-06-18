package com.inhalink.service;

import com.inhalink.domain.User;
import com.inhalink.dto.request.UserProfileCreateRequest;
import com.inhalink.dto.request.UserProfileUpdateRequest;
import com.inhalink.exception.UserNotFoundException;
import com.inhalink.repository.UserRepository;
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
    private final EmailService emailService;

    @Transactional
    public void signUp(SignupRequest request) {
        // 1. 이메일 인증 여부 확인
        if (!emailService.isVerified(request.getEmail())) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        // 2. 중복 체크
        validateDuplicateUser(request);
        // 3. 암호화 및 엔티티 생성
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