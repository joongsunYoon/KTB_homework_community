package com.example.community.domain.post.like;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class PostLikeId implements Serializable {
    private Long postId;
    private Long userId;
}