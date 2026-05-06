package com.inhalink.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("해당 모집글을 찾을 수 없습니다.");
    }
}
