package com.example.community.domain.post.info;

import com.example.community.domain.post.Post;
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
    @Column(name = "post_id", columnDefinition = "INT UNSIGNED")
    private Long postId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "post_id", columnDefinition = "INT UNSIGNED")
    private Post post;

    @Column(name = "view_count", nullable = false, columnDefinition = "INT UNSIGNED")
    private long viewCount;

    @Column(name = "like_count", nullable = false, columnDefinition = "INT UNSIGNED")
    private long likeCount;

    @Column(name = "comment_count", nullable = false, columnDefinition = "INT UNSIGNED")
    private long commentCount;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public PostInfo(Post post) {
        this.post = post;
        this.viewCount = 0L;
        this.likeCount = 0L;
        this.commentCount = 0L;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

}
