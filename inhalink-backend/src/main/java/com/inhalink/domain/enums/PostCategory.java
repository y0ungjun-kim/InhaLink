package com.inhalink.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostCategory {
    CONTEST("공모전"),
    TEAM_PROJECT("팀플"),
    PROJECT("프로젝트");

    private final String description;
}
