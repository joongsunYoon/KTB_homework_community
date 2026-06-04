package com.example.community.global.exception;

public record ErrorResponse(
        String message,
        Object data
) {
}