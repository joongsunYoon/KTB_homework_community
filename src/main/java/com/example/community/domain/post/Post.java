package com.example.community.domain.post;

import com.example.community.domain.image.Image;
import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "post", cascade = CascadeType.REMOVE)
    private PostInfo postInfo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("postOrder ASC")
    private List<Image> images = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Builder
    public Post(Long categoryId, User user, String title, String content) {
        this.categoryId = categoryId;
        this.user  = user;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid_request");
        }
        this.title = title;
        this.content = content;
    }

    public String getPostImageUrl() {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.get(0).getUrl();
    }
}
