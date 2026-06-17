package com.example.community.domain.post;

import com.example.community.domain.comment.CommentRepository;
import com.example.community.domain.post.dto.PostListResponse;
import com.example.community.domain.post.dto.PostResponse;
import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.post.info.PostInfoRepository;
import com.example.community.domain.post.like.PostLikeRepository;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.global.exception.ForbiddenException;
import com.example.community.global.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final PostInfoRepository postInfoRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository,
            CommentRepository commentRepository,
            PostInfoRepository postInfoRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postInfoRepository = postInfoRepository;
    }

    @Transactional
    public PostResponse createPost(String title, String content, long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("유저를 찾을 수 없습니다.",null)
        );

        Post post = Post.builder()
                .categoryId(1L) // 카테고리까지는 무리여서 일단 그냥 1로 더미데이터로 생성
                .user(user)
                .title(title)
                .content(content)
                .build();

        PostInfo postInfo = new PostInfo(post);
        postInfoRepository.save(postInfo);
        return PostResponse.from(postRepository.save(post),user,postInfo);
    }

    public List<PostListResponse> getPostList(int cursor, int size) {
        long currentCursor = (cursor == 0) ? Integer.MAX_VALUE : cursor;

        List<Post> posts = postRepository.findTopPostsByCursor(currentCursor, PageRequest.of(0, size));
        return posts.stream()
                .map(post -> {
                    User user = post.getUser();
                    PostInfo postInfo = post.getPostInfo();
                    return PostListResponse.from(post, user , postInfo);
                })
                .collect(Collectors.toList());
    }

    // Post + PostInfo
    // todo: postInfo 조회수 증가 로직 비동기화 해야함.
    @Transactional
    public PostResponse getPostDetail(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다.", null));

        User user = post.getUser();
        PostInfo postInfo = post.getPostInfo();
        postInfo.increaseViewCount();
        postInfoRepository.save(postInfo);

        return PostResponse.from(post, user, postInfo);
    }

    @Transactional
    public void update(long postId, String title, String content, long loginUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!post.getUser().getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        post.update(title, content);
        postRepository.save(post);
    }

    //Post + PostInfo
    @Transactional
    public void remove(long postId, long loginUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!post.getUser().getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);
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