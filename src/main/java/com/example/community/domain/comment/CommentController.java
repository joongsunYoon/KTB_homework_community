package com.example.community.domain.comment;

import com.example.community.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable int postId,
                                                             @RequestBody Map<String, String> body,
                                                             HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = getLoginUserOrThrow(request);

        commentService.createComment(postId, loginUser.getUserId(), body.get("content"));

        response.put("message", "comment_created");
        response.put("data", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable int postId,
                                                             @PathVariable int commentId,
                                                             @RequestBody Map<String, String> body,
                                                             HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = getLoginUserOrThrow(request);

        commentService.updateComment(commentId, body.get("content"), loginUser.getUserId());

        response.put("message", "comment_updated");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable int postId,
                                                             @PathVariable int commentId,
                                                             HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        User loginUser = getLoginUserOrThrow(request);

        commentService.removeComment(commentId, loginUser.getUserId());

        response.put("message", "comment_deleted");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    private User getLoginUserOrThrow(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
            throw new IllegalArgumentException("로그인이 되어있지 않습니다. 로그인을 해주세요");
        }
        return (User) session.getAttribute("LOGIN_USER");
    }
}