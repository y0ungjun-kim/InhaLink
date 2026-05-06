package com.inhalink.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProjectPostCreateRequest {

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String content;

    @NotNull(message = "마감일은 필수 입력 사항입니다.")
    @Future(message = "마감일은 현재 시간 이후여야 합니다.") // 과거 시간 입력 예외 처리
    private LocalDateTime deadline;
}