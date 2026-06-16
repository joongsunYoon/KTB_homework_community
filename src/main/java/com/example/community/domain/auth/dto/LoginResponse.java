package com.example.community.domain.auth.dto;

import com.example.community.domain.user.entity.User;

public record LoginResponse(
        String accessToken
) {
}
