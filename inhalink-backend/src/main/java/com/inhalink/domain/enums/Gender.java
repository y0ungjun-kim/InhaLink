package com.inhalink.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String description; // 한글 설명 나중에 프론트에 넘겨줄 때 사용
}
