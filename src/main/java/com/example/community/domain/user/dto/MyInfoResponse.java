package com.example.community.domain.user.dto;

import com.example.community.domain.user.entity.User;

public record MyInfoResponse(
        Long userId,
        String email,
        String nickname,
        String profileImageUrl
) {
    public static MyInfoResponse from(User user) {
        return new MyInfoResponse(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}
