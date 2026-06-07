package com.example.community.domain.post;

import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", columnDefinition = "INT UNSIGNED")
    private Long postId;

    @Column(name = "category_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "post", cascade = CascadeType.REMOVE)
    private PostInfo postInfo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_image_url")
    private String postImageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Builder
    public Post(Long categoryId, User user, String title, String content, String postImageUrl) {
        this.categoryId = categoryId;
        this.user  = user;
        this.title = title;
        this.content = content;
        this.postImageUrl = postImageUrl;
    }

    public void update(String title, String content) {
        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid_request");
        }
        this.title = title;
        this.content = content;
    }

    public void updatePostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }
}