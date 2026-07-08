package com.example.community.domain.post.dto;

import com.example.community.domain.post.Post;
import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.post.info.PostInfoResponse;
import com.example.community.domain.user.entity.User;

import java.text.SimpleDateFormat;
import java.util.List;

public record PostResponse(
        Long postId,
        String title,
        String content,
        List<String> postImages,
        String nickname,
        String profileImage,
        PostInfoResponse postInfoResponse,
        String createdAt,
        String updatedAt
) {
    public static PostResponse from(Post post, User user, PostInfo postInfo, List<String> postImages) {
        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m:s");

        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                postImages != null ? postImages : List.of(),
                user != null ? user.getNickname() : "알 수 없는 사용자",
                user != null ? user.getProfileImageUrl() : null,
                PostInfoResponse.from(postInfo),
                post.getCreatedAt() != null ? sdf.format(post.getCreatedAt()) : null,
                post.getUpdatedAt() != null ? sdf.format(post.getUpdatedAt()) : null
        );
    }
}
