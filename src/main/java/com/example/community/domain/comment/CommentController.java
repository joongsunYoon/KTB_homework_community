package com.example.community.domain.comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getComments(@PathVariable int postId) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> comments = commentService.getCommentsByPost(postId);

        Map<String, Object> data = new HashMap<>();
        data.put("comments", comments);

        response.put("message", "comment_search_success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(
            @PathVariable long postId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Long userId
    ) {
        Map<String, Object> response = new HashMap<>();

        commentService.createComment(postId, userId, body.get("content"));

        response.put("message", "comment_created");
        response.put("data", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable long postId,
            @PathVariable long commentId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Long userId
    ) {

        Map<String, Object> response = new HashMap<>();
        commentService.updateComment(commentId, body.get("content"), userId);

        response.put("message", "comment_updated");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable long postId,
            @PathVariable long commentId,
            @AuthenticationPrincipal Long userId
    ) {

        Map<String, Object> response = new HashMap<>();
        commentService.removeComment(commentId, userId);

        response.put("message", "comment_deleted");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }
}