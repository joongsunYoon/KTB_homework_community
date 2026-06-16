package com.example.community.domain.auth;

import com.example.community.domain.auth.dto.TokenResponse;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.service.UserService;
import com.example.community.global.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse httpResponse
    ) {
        User loginUser = userService.login(request.getEmail(), request.getPassword());

        String accessToken = jwtProvider.generateAccessToken(loginUser);
        String refreshToken = jwtProvider.generateRefreshToken(loginUser);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        httpResponse.addCookie(refreshCookie);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "login_success");
        response.put("data", null);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> logout(
            HttpServletResponse httpResponse
    ) {

        Cookie killCookie = new Cookie("refresh_token", null);
        killCookie.setHttpOnly(true);
        killCookie.setSecure(true);
        killCookie.setPath("/");
        killCookie.setMaxAge(0);
        httpResponse.addCookie(killCookie);

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponse> refresh(
            @CookieValue(name = "refresh_token", required = false)
            String refreshToken,
            HttpServletResponse httpResponse
    ) {
        TokenResponse response = authService.refresh(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + response.accessToken());

        Cookie refreshCookie = new Cookie("refresh_token", response.refreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        httpResponse.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(response);
    }
}