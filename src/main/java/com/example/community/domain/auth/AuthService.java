package com.example.community.domain.auth;

import com.example.community.domain.auth.dto.LoginResponse;
import com.example.community.domain.auth.dto.TokenResponse;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.global.exception.NotFoundException;
import com.example.community.global.exception.UnauthorizedException;
import com.example.community.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;


    public TokenResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        if (!user.getPasswordHash().equals(password)) throw new IllegalArgumentException("invalid_request");

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return new TokenResponse(accessToken,refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {

        if (refreshToken == null) {
            throw new UnauthorizedException("refreshToken이 없습니다." , null);
        }

        Long userId = jwtProvider.validateToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다." , null));

        //todo: refreshToken 정보 삭제 + 생성 후 저장
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        return new TokenResponse(newAccessToken , newRefreshToken);
    }


}
