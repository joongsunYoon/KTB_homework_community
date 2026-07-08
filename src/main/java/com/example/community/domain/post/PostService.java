package com.example.community.domain.post;

import com.example.community.domain.comment.CommentRepository;
import com.example.community.domain.image.Image;
import com.example.community.domain.image.ImageRepository;
import com.example.community.domain.image.ImageService;
import com.example.community.domain.image.ImageType;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostInfoRepository postInfoRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            PostLikeRepository postLikeRepository,
            CommentRepository commentRepository,
            PostInfoRepository postInfoRepository,
            ImageRepository imageRepository,
            ImageService imageService
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postInfoRepository = postInfoRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    @Transactional
    public PostResponse createPost(
            String title,
            String content,
            List<String> imageUrls,
            long userId
    ) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("유저를 찾을 수 없습니다.", null)
        );

        Post post = Post.builder()
                .categoryId(1L) // 카테고리까지는 무리여서 일단 그냥 1로 더미데이터로 생성
                .user(user)
                .title(title)
                .content(content)
                .build();
        postRepository.save(post);

        PostInfo postInfo = new PostInfo(post);
        postInfoRepository.save(postInfo);

        List<String> postImageUrls = getPostImageUrls(imageUrls);
        imageService.createPostImages(post, postImageUrls);

        return PostResponse.from(post, user, postInfo, postImageUrls);
    }

    public List<PostListResponse> getPostList(int cursor, int size) {
        long currentCursor = (cursor == 0) ? Integer.MAX_VALUE : cursor;

        List<Post> posts = postRepository.findTopPostsByCursor(currentCursor, PageRequest.of(0, size));
        if (posts.isEmpty()) {
            return List.of();
        }

        List<Long> postIds = posts.stream()
                .map(Post::getPostId)
                .toList();
        Map<Long, String> thumbnailImages = imageRepository
                .findPostThumbnails(postIds, ImageType.POST)
                .stream()
                .collect(Collectors.toMap(
                        image -> image.getPost().getPostId(),
                        Image::getUrl
                ));

        return posts.stream()
                .map(post -> PostListResponse.from(
                        post,
                        post.getUser(),
                        post.getPostInfo(),
                        thumbnailImages.get(post.getPostId())
                ))
                .toList();
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

        List<String> postImages = post.getImages().stream()
                .map(Image::getUrl)
                .toList();

        return PostResponse.from(post, user, postInfo , postImages);
    }

    @Transactional
    public void update(long postId, String title, String content, long loginUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!post.getUser().getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        post.update(title, content);
        postRepository.save(post);
    }

    @Transactional
    public void remove(long postId, long loginUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!post.getUser().getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);
        postRepository.delete(post);
    }

    private List<String> getPostImageUrls(List<String> imageUrls) {
        if (imageUrls == null) {
            return List.of();
        }

        return imageUrls.stream()
                .filter(url -> url != null && !url.isBlank())
                .distinct()
                .toList();
    }
}