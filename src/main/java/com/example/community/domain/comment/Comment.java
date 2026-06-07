package com.example.community.domain.comment;

import com.example.community.domain.post.Post;
import com.example.community.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(nullable = false, length = 1000)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Builder
    public Comment(Long postId, User user, Long parentCommentId, String content) {
        this.postId = postId;
        this.user = user;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public void updateContent(String newContent) {
        // todo: 이것은 dto로 책임분리해야함.
        if (newContent == null || newContent.trim().isEmpty() || newContent.length() > 1000) {
            throw new IllegalArgumentException("invalid_request");
        }
        this.content = newContent;
    }
}