package com.example.community.domain.post.info;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "post_info")
@NoArgsConstructor
@Getter
public class PostInfo {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    @Column(name = "like_count", nullable = false)
    private long likeCount;

    @Column(name = "comment_count", nullable = false)
    private long commentCount;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public PostInfo(Long postId) {
        this.postId = postId;
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.commentCount = 0L;
    }



}
