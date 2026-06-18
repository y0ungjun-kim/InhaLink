package com.inhalink.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingStatus {
    WAITING("대기 중"),
    MATCHED("매칭 완료"),
    CANCELLED("취소됨");

    private final String description;
}
