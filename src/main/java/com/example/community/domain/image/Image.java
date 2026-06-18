package com.example.community.domain.image;

import com.example.community.domain.post.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", columnDefinition = "INT UNSIGNED")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String url;

    @Column
    private String path;

    @Column(name = "content_type")
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false, length = 30)
    private ImageType imageType;

    @Column(name = "post_order", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer postOrder;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Builder
    public Image(
            Post post,
            String name,
            String url, String path,
            String contentType, ImageType imageType,
            Integer postOrder
    ) {
        this.post = post;
        this.name = name;
        this.url = url;
        this.path = path;
        this.contentType = contentType;
        this.imageType = imageType;
        this.postOrder = postOrder == null ? 0 : postOrder;
    }
}
