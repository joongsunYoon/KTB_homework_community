package com.example.community.global.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final Object data;

    public NotFoundException(String message, Object data) {
        super("데이터가 없습니다.");
        this.data = data;
    }
}