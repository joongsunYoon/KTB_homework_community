package com.example.community.domain.post.like;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostIdAndUserId(long postId, long userId);
    boolean existsByPostIdAndUserId(long postId, long userId);
}