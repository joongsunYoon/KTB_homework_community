package com.example.community.domain.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user  WHERE p.postId < :cursor ORDER BY p.postId DESC")
    List<Post> findTopPostsByCursor(@Param("cursor") long cursor, Pageable pageable);

    long countByPostId(Long postId);
}