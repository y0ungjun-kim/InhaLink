package com.inhalink.dto.request;

import com.inhalink.domain.enums.ActivityMethod;
import com.inhalink.domain.enums.PostCategory;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
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

    @NotNull(message = "카테고리는 필수 입력 사항입니다.")
    private PostCategory category;

    @NotBlank(message = "프로젝트/공모전 이름은 필수 입력 사항입니다.")
    private String projectName;

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String content;

    @Min(value = 1, message = "모집 인원은 1명 이상이어야 합니다.")
    private int maxMembers;

    @NotNull(message = "모집 마감일은 필수 입력 사항입니다.")
    @Future(message = "모집 마감일은 현재 시간 이후여야 합니다.")
    private LocalDateTime deadline;

    // 팀 결성 희망일은 선택
    private LocalDateTime teamFormationDate;

    // 우대사항은 선택
    private String preferredQualifications;

    // 하고 싶은 말은 선택
    private String message;

    @NotNull(message = "활동 방식은 필수 입력 사항입니다.")
    private ActivityMethod activityMethod;
}