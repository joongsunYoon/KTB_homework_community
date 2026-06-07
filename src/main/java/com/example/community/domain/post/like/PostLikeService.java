package com.example.community.domain.post.like;

import com.example.community.domain.post.Post;
import com.example.community.domain.post.PostRepository;
import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.post.info.PostInfoRepository;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostInfoRepository postInfoRepository;


    // PostLike + PostInfo
    @Transactional
    public void addLike(long postId, long userId) {

        //PostLike 생성
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalArgumentException("invalid_request");
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("게시글을 찾을 수 없습니다.",null)
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("유저를 찾을 수 없습니다.",null)
        );

        PostLike postLike = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        postLikeRepository.save(postLike);

        //PostInfo 좋아요 수 +1

        PostInfo postInfo = postInfoRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("게시글 정보를 찾을 수 없습니다.",null)
        );

        postInfo.increaseLikeCount();
        postInfoRepository.save(postInfo);

    }

    // PostLike + PostInfo
    @Transactional
    public void removeLike(long postId, long userId) {

        //PostLike 삭제
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("invalid_request"));

        postLikeRepository.delete(postLike);

        //PostInfo 좋아요 수 -1
        PostInfo postInfo = postInfoRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("게시글 정보를 찾을 수 없습니다.",null)
        );

        postInfo.decreaseLikeCount();
        postInfoRepository.save(postInfo);
    }
}