package com.inhalink.controller;

import com.inhalink.domain.User;
import com.inhalink.config.JwtUtil;
import com.inhalink.dto.request.LoginRequest;
import com.inhalink.dto.request.SignupRequest;
import com.inhalink.dto.request.UserProfileCreateRequest;
import com.inhalink.dto.request.UserProfileUpdateRequest;
import com.inhalink.dto.response.ApiResponse;
import com.inhalink.dto.response.LoginResponse;
import com.inhalink.dto.response.UserProfileResponse;
import com.inhalink.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API", description = "사용자 프로필 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignupRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", null));
    }

    @Operation(summary = "로그인", description = "학번과 비밀번호로 로그인합니다. 응답에 JWT 토큰과 profileComplete 포함.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request);
        String token = jwtUtil.generateToken(user.getStudentId());
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", new LoginResponse(token, user)));
    }

    @Operation(summary = "내 정보 조회 (토큰 기반)", description = "JWT 토큰에서 학번을 추출해 프로필을 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMe(
            org.springframework.security.core.Authentication authentication) {
        String studentId = (String) authentication.getPrincipal();
        User user = userService.getProfile(studentId);
        return ResponseEntity.ok(ApiResponse.success("조회 성공", new UserProfileResponse(user)));
    }

    @Operation(summary = "프로필 조회", description = "학번으로 사용자 프로필을 조회합니다. profileComplete가 false면 최초 프로필 작성 화면으로 이동해야 합니다.")
    @GetMapping("/{studentId}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @PathVariable String studentId) {

        User user = userService.getProfile(studentId);
        return ResponseEntity.ok(ApiResponse.success("조회 성공", new UserProfileResponse(user)));
    }

    @Operation(summary = "최초 프로필 작성", description = "로그인 후 최초 1회 필수 항목을 모두 입력해 프로필을 완성합니다. 이미 완성된 경우 409 에러를 반환합니다.")
    @PostMapping("/{studentId}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> createProfile(
            @PathVariable String studentId,
            @Valid @RequestBody UserProfileCreateRequest request) {

        User user = userService.createProfile(studentId, request);
        return ResponseEntity.ok(ApiResponse.success("프로필 작성 완료", new UserProfileResponse(user)));
    }

    @Operation(summary = "마이페이지 프로필 수정", description = "수정할 필드만 보내도 작동합니다. (부분 업데이트)")
    @PutMapping("/{studentId}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @PathVariable String studentId,
            @RequestBody UserProfileUpdateRequest request) {

        User updatedUser = userService.updateUserProfile(studentId, request);
        return ResponseEntity.ok(ApiResponse.success("프로필 수정 성공", new UserProfileResponse(updatedUser)));
    }
}
