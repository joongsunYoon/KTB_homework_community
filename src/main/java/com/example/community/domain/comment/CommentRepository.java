package com.example.community.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c.commentId as commentId, c.content as content, " +
            "u.nickname as nickname, u.profileImageUrl as profileImage, " +
            "c.createdAt as createdAt, c.updatedAt as updatedAt " +
            "FROM Comment c " +
            "JOIN c.user u " +
            "WHERE c.postId = :postId " +
            "ORDER BY c.commentId ASC")
    List<Map<String, Object>> findAllByPostId(@Param("postId") long postId);
}