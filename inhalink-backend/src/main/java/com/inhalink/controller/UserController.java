package com.inhalink.controller;

import com.inhalink.domain.User;
import com.inhalink.dto.request.UserProfileUpdateRequest;
import com.inhalink.dto.response.ApiResponse;
import com.inhalink.dto.response.UserProfileResponse;
import com.inhalink.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User API", description = "사용자 프로필 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "프로필 작성 및 수정", description = "최초 프로필 작성 또는 마이페이지에서 프로필을 수정합니다. 수정할 필드만 보내도 작동합니다.")
    @PutMapping("/{studentId}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateUserProfile(
            @PathVariable String studentId,
            @Valid @RequestBody UserProfileUpdateRequest request) {

        // 1. 서비스 호출 및 업데이트된 유저 정보 받아오기
        User updatedUser = userService.updateUserProfile(studentId, request);

        // ApiResponse로 감싸서 반환
        UserProfileResponse data = new UserProfileResponse(updatedUser);
        return ResponseEntity.ok(ApiResponse.success("프로필 수정 성공", data));
    }
}