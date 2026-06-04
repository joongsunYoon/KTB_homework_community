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

    public User findById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
    }

    @Transactional
    public void create(CreateRequestDto dto) {
        if (!dto.password().equals(dto.passwordCheck())) throw new IllegalArgumentException("invalid_request");
        if (userRepository.findByEmail(dto.email()).isPresent()) throw new IllegalArgumentException("invalid_request");

        User user = User.builder()
                .email(dto.email())
                .passwordHash(dto.password())
                .nickname(dto.nickname())
                .build();
        userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        if (!user.getPasswordHash().equals(password)) throw new IllegalArgumentException("invalid_request");
        return user;
    }

    @Transactional
    public void update(int userId, String nickname, String passwordCheck) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));

    }

    @Transactional
    public void remove(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        userRepository.delete(user);
    }

    @Transactional
    public void createOrUpdateProfileImage(int userId, MultipartFile file) {
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
    public void removeProfileImage(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));
        user.updateProfileImage(null);
    }
}