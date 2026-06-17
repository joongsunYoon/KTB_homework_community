package com.example.community.domain.user.controller;

import com.example.community.domain.user.dto.CreateRequestDto;
import com.example.community.domain.user.dto.MyInfoResponse;
import com.example.community.domain.user.dto.UpdatePasswordRequest;
import com.example.community.domain.user.dto.UpdateProfileRequest;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.service.UserService;
import com.example.community.global.ApiResponse;
import com.example.community.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MyInfoResponse>> getMyInfo(
            @AuthenticationPrincipal Long loginUserId
    ) {
        User user = userService.findById(loginUserId);
        return ResponseEntity.ok(ApiResponse.success(MyInfoResponse.from(user)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<MyInfoResponse>> search(
            @PathVariable long userId,
            @AuthenticationPrincipal Long loginUserId
    ) {
        if (loginUserId != userId) {
            throw new ForbiddenException("사용자 권한이 없습니다.", null);
        }

        User userData = userService.findById(userId);
        return ResponseEntity.ok(ApiResponse.success(MyInfoResponse.from(userData)));
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
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @PathVariable long userId,
            @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal Long loginUserId
    ) {
        if (loginUserId != userId) {
            throw new ForbiddenException("사용자 권한이 없습니다.", null);
        }

        userService.updateProfile(userId, request.nickname(), request.profileImageUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @PathVariable long userId,
            @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal Long loginUserId
    ) {
        if (loginUserId != userId) {
            throw new ForbiddenException("사용자 권한이 없습니다.", null);
        }

        userService.updatePassword(userId, request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success());
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

    @GetMapping("/email/check")
    public ResponseEntity<Void> checkEmail(@RequestParam String email) {
        if (userService.isEmailAvailable(email)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<Void> checkNickname(@RequestParam String nickname) {
        if (userService.isNicknameAvailable(nickname)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
