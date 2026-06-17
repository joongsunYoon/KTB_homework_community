package com.example.community.domain.user.dto;

public record UpdateProfileRequest(
        String nickname,
        String profileImageUrl
) {
}
