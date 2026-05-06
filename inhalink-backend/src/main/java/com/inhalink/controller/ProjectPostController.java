package com.inhalink.controller;

import com.inhalink.dto.request.ProjectPostCreateRequest;
import com.inhalink.dto.response.ApiResponse;
import com.inhalink.dto.response.PostCreateResponse;
import com.inhalink.service.ProjectPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class ProjectPostController {

    private final ProjectPostService projectPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostCreateResponse>> createPost(
            // 실제 배포 시에는 로그인된 유저 정보를 SecurityContext에서 가져와야 하지만, 현재는 파라미터로 받습니다.
            @RequestParam String studentId,
            @Valid @RequestBody ProjectPostCreateRequest request) {

        // 1. 서비스 호출 후 새로 생성된 글의 ID 받아오기
        Long newPostId = projectPostService.createPost(studentId, request);

        // // ApiResponse로 감싸서 반환
        PostCreateResponse data = new PostCreateResponse(newPostId, "모집글이 성공적으로 작성되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("성공", data));
    }
}