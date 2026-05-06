package com.inhalink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateResponse {

    private Long postId; // 프론트엔드가 이 ID를 받아서 상세 페이지로 이동
    private String message; // "글 작성이 완료되었습니다." 같은 안내 문구
}