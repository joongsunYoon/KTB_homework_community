package com.example.community.domain.user.service;

import com.example.community.domain.user.dto.CreateRequestDto;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/upload-images/";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
    public void update(long userId, String nickname, String passwordCheck) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        if(user.getNickname() != null) user.updateNickname(nickname);
        if(user.getPasswordHash() == null) user.updatePassword(passwordCheck);
        userRepository.save(user);

    }

    @Transactional
    public void remove(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        userRepository.delete(user);
    }

    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void createOrUpdateProfileImage(long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = "user_" + userId + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

            user.updateProfileImage("image-server/users/" + userId + "/profiles-image");
        } catch (Exception e) {
            throw new RuntimeException("internal_server_error");
        }
    }

    @Transactional
    public void removeProfileImage(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        user.updateProfileImage(null);
    }
}