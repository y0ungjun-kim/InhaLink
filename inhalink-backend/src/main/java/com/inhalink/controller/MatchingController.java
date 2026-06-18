package com.inhalink.controller;

import com.inhalink.dto.response.ApiResponse;
import com.inhalink.dto.response.MatchingResponse;
import com.inhalink.service.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Matching API", description = "1대1 즉시 매칭 관련 API")
@RestController
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @Operation(summary = "매칭 참여 또는 상태 확인", description = "대기 중인 유저가 있으면 즉시 매칭, 없으면 대기열 등록. 이미 요청 중이면 현재 상태 반환.")
    @PostMapping
    public ResponseEntity<ApiResponse<MatchingResponse>> joinOrCheck(@RequestParam String studentId) {
        MatchingResponse response = matchingService.joinOrCheck(studentId);
        return ResponseEntity.ok(ApiResponse.success("성공", response));
    }

    @Operation(summary = "매칭 상태 폴링", description = "현재 매칭 상태를 조회합니다. 프론트에서 3초마다 호출해 MATCHED 여부를 확인합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<MatchingResponse>> getStatus(@RequestParam String studentId) {
        MatchingResponse response = matchingService.getStatus(studentId);
        return ResponseEntity.ok(ApiResponse.success("성공", response));
    }

    @Operation(summary = "매칭 취소", description = "대기열에서 이탈합니다.")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> cancel(@RequestParam String studentId) {
        matchingService.cancel(studentId);
        return ResponseEntity.ok(ApiResponse.success("매칭이 취소되었습니다.", null));
    }
}
