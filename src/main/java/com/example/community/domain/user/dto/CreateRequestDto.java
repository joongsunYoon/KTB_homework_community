package com.example.community.domain.user.dto;

public record CreateRequestDto(
        String email,
        String password,
        String passwordCheck,
        String nickname
) {
}
