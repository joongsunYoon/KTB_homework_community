package com.example.community.domain.post.dto;

import java.util.List;

public record CreatePostRequest(
        String title,
        String content,
        List<String> imageUrls
) {
}
