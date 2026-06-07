package com.example.community.domain.post.dto;

import com.example.community.domain.post.Post;
import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.post.info.PostInfoResponse;
import com.example.community.domain.user.entity.User;

import java.text.SimpleDateFormat;

public record PostResponse(
        Long postId,
        String title,
        String content,
        String image,
        String nickname,
        String profileImage,
        PostInfoResponse postInfoResponse,
        String createdAt,
        String updatedAt
) {
    public static PostResponse from(Post post, User user , PostInfo postInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m:s");

        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getPostImageUrl() != null ? post.getPostImageUrl() : "image-server/post/image",
                user.getNickname(),
                user.getProfileImageUrl(),
                PostInfoResponse.from(postInfo),
                post.getCreatedAt() != null ? sdf.format(post.getCreatedAt()) : null,
                post.getUpdatedAt() != null ? sdf.format(post.getUpdatedAt()) : null
        );
    }


}