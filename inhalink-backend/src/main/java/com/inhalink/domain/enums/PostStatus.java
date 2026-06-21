package com.inhalink.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
    RECRUITING("모집 중"),
    CLOSED("모집 마감"),
    DISABLED("비활성화"),
    DELETED("삭제");

    private final String description; // 한글 설명 나중에 프론트에 넘겨줄 때 사용
}