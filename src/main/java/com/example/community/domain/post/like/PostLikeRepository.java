package com.example.community.domain.post.like;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    Optional<PostLike> findByPostIdAndUserId(int postId, int userId);
    boolean existsByPostIdAndUserId(int postId, int userId);
    long countByPostId(int postId);
}