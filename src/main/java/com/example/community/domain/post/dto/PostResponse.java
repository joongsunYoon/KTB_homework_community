package com.example.community.domain.post.dto;

import com.example.community.domain.post.Post;
import com.example.community.domain.user.entity.User;

import java.text.SimpleDateFormat;

public record PostResponse(
        Long postId,
        String title,
        String content,
        String image,
        String nickname,
        String profileImage,
        long likeCount,
        long viewCount,
        long commentCount,
        String createdAt,
        String updatedAt
) {
    public static PostResponse from(Post post, User user, long likeCount, long commentCount) {
        SimpleDateFormat sdf = new SimpleDateFormat("Y-m-d H:m:s");

        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getPostImageUrl() != null ? post.getPostImageUrl() : "image-server/post/image",
                user.getNickname(),
                user.getProfileImageUrl(),
                likeCount,
                post.getViewCount(),
                commentCount,
                post.getCreatedAt() != null ? sdf.format(post.getCreatedAt()) : null,
                post.getUpdatedAt() != null ? sdf.format(post.getUpdatedAt()) : null
        );
    }


}