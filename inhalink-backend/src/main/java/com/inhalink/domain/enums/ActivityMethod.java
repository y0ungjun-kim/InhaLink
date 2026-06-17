package com.inhalink.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityMethod {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    BOTH("온/오프라인 병행");

    private final String description;
}
