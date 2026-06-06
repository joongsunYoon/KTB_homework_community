package com.example.community.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRequestDto(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String passwordCheck,
        @NotBlank String nickname
) {
}
