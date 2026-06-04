package com.example.community.global.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
    private final Object data;

    public ForbiddenException(String message, Object data) {
        super("사용자 권한이 없습니다.");
        this.data = data;
    }
}
