package com.example.community.domain.post.info;

import com.example.community.domain.post.Post;
import com.example.community.domain.user.entity.User;

public record PostInfoResponse(
        long likeCount,
        long viewCount,
        long commentCount
) {
    public static PostInfoResponse from(PostInfo postInfo) {
        return new PostInfoResponse(
                postInfo.getLikeCount(),
                postInfo.getViewCount(),
                postInfo.getCommentCount()
        );
    }
}
