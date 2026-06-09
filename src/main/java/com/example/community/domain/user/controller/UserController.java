package com.example.community.domain.user.controller;

import com.example.community.domain.user.dto.CreateRequestDto;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.service.UserService;
import com.example.community.global.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> search(
            @PathVariable long userId,
            @AuthenticationPrincipal Long loginUserId
    ) {
        Map<String, Object> response = new HashMap<>();

        if (loginUserId != userId) {
            response.put("message", "사용자 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        User userData = userService.findById(userId);

        response.put("message", "user_found");
        response.put("data", userData);
        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody CreateRequestDto dto) {
        Map<String, Object> response = new HashMap<>();
        userService.create(dto);
        response.put("message", "user_created");
        response.put("data", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable long userId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Long loginUserId
    ) {
        Map<String, Object> response = new HashMap<>();

        if (loginUserId != userId) {
            response.put("message", "사용자 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        userService.update(userId, body.get("nickname"), body.get("passwordCheck"));
        response.put("message", "user_updated");
        response.put("data", null);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable long userId,
            @AuthenticationPrincipal Long loginUserId
    ) {

        if (loginUserId != userId) {
            throw new ForbiddenException("사용자 권한이 없습니다.", null);
        }

        userService.remove(userId);
        return ResponseEntity.noContent().build();
    }
}