package com.example.community.domain.post.dto;

import com.example.community.domain.post.Post;
import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.post.info.PostInfoResponse;
import com.example.community.domain.user.entity.User;

public record PostListResponse(
        Long postId,
        String title,
        String image,
        String nickname,
        String profileImage,
        PostInfoResponse postInfoResponse
) {

    public static PostListResponse from(Post post, User user, PostInfo postInfo) {
        return new PostListResponse(
                post.getPostId(),
                post.getTitle(),
                post.getPostImageUrl() != null ? post.getPostImageUrl() : "image-server/post/image",
                user != null ? user.getNickname() : "알 수 없는 사용자",
                user != null ? user.getProfileImageUrl() : "image-server/post/image",
                PostInfoResponse.from(postInfo)
        );
    }
}