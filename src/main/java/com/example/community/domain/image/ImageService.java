package com.example.community.domain.image;

import com.example.community.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    private Image createProfileImage(String url) {
        return imageRepository.save(Image.builder()
                .name(extractName(url))
                .url(url)
                .imageType(ImageType.PROFILE)
                .build());
    }

    // 이미지 재사용
    @Transactional
    public Image getOrCreateProfileImage(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid_request");
        }

        return imageRepository.findByUrl(url)
                .orElseGet(() -> createProfileImage(url));
    }

    @Transactional
    public void createPostImages(Post post, List<String> urls) {
        if (urls == null || urls.isEmpty()) return;

        for (int i = 0; i < urls.size(); i++) {
            String url = urls.get(i);
            if (url == null || url.trim().isEmpty()) {
                throw new IllegalArgumentException("invalid_request");
            }

            imageRepository.save(Image.builder()
                    .post(post)
                    .name(extractName(url))
                    .url(url)
                    .imageType(ImageType.POST)
                    .postOrder(i)
                    .build());
        }
    }

    public String uploadImage(MultipartFile file, ImageType imageType) {
        String uploadDir = getUploadDir(imageType);
        String imageUrlPrefix = getImageUrlPrefix(imageType);
        Path filePath = saveFile(file, uploadDir);
        return imageUrlPrefix + filePath.getFileName();
    }

    private Path saveFile(MultipartFile multipartFile, String dir) {
        try {
            File file = new File(dir);
            if (!file.exists()) file.mkdirs();

            String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
            Path filePath = Paths.get(dir + fileName);
            Files.write(filePath, multipartFile.getBytes());
            return filePath;
        } catch (Exception e) {
            throw new RuntimeException("internal_server_error");
        }
    }

    private String getUploadDir(ImageType imageType) {
        if (imageType == ImageType.POST) {
            return System.getProperty("user.dir") + "/upload-posts/";
        }
        if (imageType == ImageType.PROFILE) {
            return System.getProperty("user.dir") + "/upload-images/";
        }
        throw new IllegalArgumentException("invalid_request");
    }

    private String getImageUrlPrefix(ImageType imageType) {
        if (imageType == ImageType.POST) {
            return "image-server/posts/";
        }
        if (imageType == ImageType.PROFILE) {
            return "image-server/users/";
        }
        throw new IllegalArgumentException("invalid_request");
    }

    private String extractName(String url) {
        String normalizedUrl = url.trim();
        int lastSlashIndex = normalizedUrl.lastIndexOf('/');
        String name = lastSlashIndex >= 0
                ? normalizedUrl.substring(lastSlashIndex + 1)
                : normalizedUrl;

        return name.isBlank() ? UUID.randomUUID().toString() : name;
    }
}
