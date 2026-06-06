package com.example.community.domain.post.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public PostLikeService(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public void addLike(long postId, long userId) {
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalArgumentException("invalid_request");
        }

        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(userId)
                .build();

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void removeLike(long postId, long userId) {
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));

        postLikeRepository.delete(postLike);
    }
}