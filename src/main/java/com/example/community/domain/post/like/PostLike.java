package com.example.community.domain.post.like;

import com.example.community.domain.post.Post;
import com.example.community.domain.user.entity.User;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Builder
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}