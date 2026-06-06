package com.example.community.domain.post;

import com.example.community.domain.comment.CommentRepository;
import com.example.community.domain.post.dto.PostListResponse;
import com.example.community.domain.post.dto.PostResponse;
import com.example.community.domain.post.like.PostLikeRepository;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.global.exception.ForbiddenException;
import com.example.community.global.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final String POST_UPLOAD_DIR = System.getProperty("user.dir") + "/upload-posts/";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository,
            CommentRepository commentRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void createPost(String title, String content, long userId) {
        Post post = Post.builder()
                .categoryId(1L) // 카테고리까지는 무리여서 일단 그냥 1로 더미데이터로 생성
                .userId(userId)
                .title(title)
                .content(content)
                .build();
        postRepository.save(post);
    }

    public List<PostListResponse> getPostList(int cursor, int size) {
        int currentCursor = (cursor == 0) ? Integer.MAX_VALUE : cursor;

        List<Post> posts = postRepository.findTopPostsByCursor(currentCursor, (Pageable) PageRequest.of(0, size));

        return posts.stream()
                .map(post -> {
                    User user = userRepository.findById(post.getUserId())
                            .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다.", null));
                    return PostListResponse.from(post, user);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse getPostDetail(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        User user = userRepository.findById(post.getUserId())
                .orElseThrow(() -> new NotFoundException("작성자 정보를 찾을 수 없습니다.", null));

        long likeCount = postLikeRepository.countByPostId(postId);
        long commentCount = commentRepository.countByCommentId(postId);

        return PostResponse.from(post, user, likeCount, commentCount);
    }

    @Transactional
    public void update(long postId, String title, String content, long loginUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!post.getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        post.update(title, content);
    }

    @Transactional
    public void remove(long postId, long loginUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!post.getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        postRepository.delete(post);
    }

    @Transactional
    public void uploadPostImage(long postId, MultipartFile file) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));
        try {
            File dir = new File(POST_UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = "post_" + postId + "_post_image_" + file.getOriginalFilename();
            Path filePath = Paths.get(POST_UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());

            post.updatePostImageUrl("image-server/posts/" + postId + "/post-image");
        } catch (Exception e) {
            throw new RuntimeException("internal_server_error");
        }
    }
}