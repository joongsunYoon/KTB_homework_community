package com.example.community.domain.post.like;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable int postId,
            @AuthenticationPrincipal Long userId
    ) {
        postLikeService.addLike(postId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "like_success");
        response.put("data", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeLike(
            @PathVariable long postId,
            @AuthenticationPrincipal Long userId
    ) {
        postLikeService.removeLike(postId, userId);

        return ResponseEntity.noContent().build();
    }
}