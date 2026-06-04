package com.example.community.domain.post;

import com.example.community.global.security.JwtProvider;
import com.example.community.domain.post.dto.PostListResponse;
import com.example.community.domain.post.dto.PostResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtProvider jwtProvider;

    public PostController(
            PostService postService,
            JwtProvider jwtProvider
    ) {
        this.postService = postService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getList(
            @RequestParam(defaultValue = "0") int cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PostListResponse> posts = postService.getPostList(cursor, size);

        return ResponseEntity.ok(Map.of(
                "message", "post_search_success",
                "data", Map.of("posts", posts)
        ));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getDetail(@PathVariable int postId) {
        PostResponse response = postService.getPostDetail(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) {
        Map<String, Object> response = new HashMap<>();
        String token = jwtProvider.extractTokenFromRequest(request);
        Integer userId = jwtProvider.validateToken(token);

        postService.createPost(body.get("title"), body.get("content"), userId);

        response.put("message", "post_created");
        response.put("data", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable int postId,
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) {

        String token = jwtProvider.extractTokenFromRequest(request);
        Integer userId = jwtProvider.validateToken(token);

        Map<String, Object> response = new HashMap<>();
        postService.update(postId, body.get("title"), body.get("content"), userId);

        response.put("message", "post_updated");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable int postId,
            HttpServletRequest request
    ) {

        String token = jwtProvider.extractTokenFromRequest(request);
        Integer userId = jwtProvider.validateToken(token);
        postService.remove(postId, userId);

        return ResponseEntity.noContent().build();
    }
}