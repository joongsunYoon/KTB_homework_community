package com.example.community.domain.post.like;

import com.example.community.global.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final JwtProvider jwtProvider;

    public PostLikeController(PostLikeService postLikeService, JwtProvider jwtProvider) {
        this.postLikeService = postLikeService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable int postId,
            HttpServletRequest request
    ) {
        String token = jwtProvider.extractTokenFromRequest(request);
        Integer loginUserId = jwtProvider.validateToken(token);

        postLikeService.addLike(postId, loginUserId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "like_success");
        response.put("data", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeLike(
            @PathVariable int postId,
            HttpServletRequest request
    ) {
        String token = jwtProvider.extractTokenFromRequest(request);
        Integer loginUserId = jwtProvider.validateToken(token);

        postLikeService.removeLike(postId, loginUserId);

        return ResponseEntity.noContent().build();
    }
}