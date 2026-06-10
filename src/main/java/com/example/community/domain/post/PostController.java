package com.example.community.domain.post;

import com.example.community.domain.post.dto.PostListResponse;
import com.example.community.domain.post.dto.PostResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    public PostController(
            PostService postService
    ) {
        this.postService = postService;
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
    public ResponseEntity<PostResponse> getDetail(@PathVariable long postId) {
        PostResponse response = postService.getPostDetail(postId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Long userId
    ) {
        Map<String, Object> response = new HashMap<>();
        PostResponse postResponse = postService.createPost(body.get("title"), body.get("content"), userId);

        response.put("message", "post_created");
        response.put("data", postResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable long postId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Long userId
    ) {

        Map<String, Object> response = new HashMap<>();
        postService.update(postId, body.get("title"), body.get("content"), userId);

        response.put("message", "post_updated");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable long postId,
            @AuthenticationPrincipal Long userId
    ) {
        postService.remove(postId, userId);

        return ResponseEntity.noContent().build();
    }
}