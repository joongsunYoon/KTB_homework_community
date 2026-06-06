package com.example.community.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.postId < :cursor ORDER BY p.postId DESC")
    List<Post> findTopPostsByCursor(@Param("cursor") int cursor, Pageable pageable);

    long countByPostId(Long postId);
}