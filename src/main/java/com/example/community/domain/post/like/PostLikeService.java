package com.example.community.domain.post.like;

import com.example.community.domain.post.Post;
import com.example.community.domain.post.PostRepository;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostLikeService(
            PostLikeRepository postLikeRepository,
            UserRepository userRepository,
            PostRepository postRepository
    ) {
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public void addLike(long postId, long userId) {
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalArgumentException("invalid_request");
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("게시글을 찾을 수 없습니다.",null)
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("유저를 찾을 수 없습니다.",null)
        );

        PostLike postLike = PostLike.builder()
                .post(post)
                .user(user)
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