package com.example.community.domain.user.service;

import com.example.community.domain.image.ImageService;
import com.example.community.domain.user.dto.CreateRequestDto;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
    }

    // todo: 비밀번호를 plain text로 저장하고 있음. papper + salt 형식으로 수정해야함.
    @Transactional
    public void create(CreateRequestDto dto) {
        User user = User.builder()
                .email(dto.email())
                .passwordHash(dto.password())
                .nickname(dto.nickname())
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void updateProfile(long userId, String nickname, String profileImageUrl) {
        User user = findById(userId);

        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid_request");
        }

        user.updateNickname(nickname);
        if (profileImageUrl == null) {
            user.updateProfileImage(null);
        } else {
            user.updateProfileImage(imageService.getOrCreateProfileImage(profileImageUrl));
        }
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(long userId, String password) {
        User user = findById(userId);

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid_request");
        }

        user.updatePassword(password);
        userRepository.save(user);
    }

    @Transactional
    public void remove(long userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
}
