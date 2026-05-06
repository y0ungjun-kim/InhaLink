package com.inhalink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;       // HTTP 상태 코드 (예: 200, 404)
    private String message;   // 결과 메시지
    private T data;           // 실제 데이터 (없을 경우 null)

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static ApiResponse<Void> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}