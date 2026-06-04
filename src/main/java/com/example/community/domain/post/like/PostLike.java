package com.example.community.domain.post.like;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;


@Entity
@Table(name = "post_like")
@IdClass(PostLikeId.class)
@Getter
@NoArgsConstructor
public class PostLike {

    @Id
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Builder
    public PostLike(Integer postId, Integer userId) {
        this.postId = postId;
        this.userId = userId;
    }
}