package com.example.community.domain.post.dto;


import com.example.community.domain.post.Post;
import com.example.community.domain.user.entity.User;

public record PostListResponse(
        Integer postId,
        String title,
        String image,
        String nickname,
        String profileImage
) {

    public static PostListResponse from(Post post, User user) {
        return new PostListResponse(
                post.getPostId(),
                post.getTitle(),
                post.getPostImageUrl() != null ? post.getPostImageUrl() : "image-server/post/image",
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}