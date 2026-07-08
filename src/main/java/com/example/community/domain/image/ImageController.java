package com.example.community.domain.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/posts")
    public ResponseEntity<Map<String, Object>> uploadPost(
            @RequestParam("postFile") MultipartFile file
    ) {
        return uploadImage(file, ImageType.POST, "fileUrl");
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> uploadUser(
            @RequestParam("profileImage") MultipartFile file
    ) {
        return uploadImage(file, ImageType.PROFILE, "profileImageUrl");
    }

    private ResponseEntity<Map<String, Object>> uploadImage(
            MultipartFile file,
            ImageType imageType,
            String responseKey
    ) {
        String imageUrl = imageService.uploadImage(file, imageType);

        return ResponseEntity.ok(Map.of(
                "message", "file_uploaded",
                "data", Map.of(responseKey, imageUrl)
        ));
    }
}
