package com.example.community.domain.post.dto;


import com.example.community.domain.post.Post;
import com.example.community.domain.user.entity.User;

public record PostListResponse(
        Long postId,
        String title,
        String image,
        String nickname,
        String profileImage
) {

    public static PostListResponse from(Post post, User user) {
        Long userId = null;

        if(user != null){
            userId = user.getUserId();
        }
        return new PostListResponse(
                post.getPostId(),
                post.getTitle(),
                post.getPostImageUrl() != null ? post.getPostImageUrl() : "image-server/post/image",
                userId != null ? user.getNickname() : "알 수 없는 사용자",
                userId != null ? user.getProfileImageUrl() : "image-server/post/image"
        );
    }
}