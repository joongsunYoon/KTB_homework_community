package com.example.community.domain.comment;

import com.example.community.domain.post.info.PostInfo;
import com.example.community.domain.post.info.PostInfoRepository;
import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.global.exception.ForbiddenException;
import com.example.community.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostInfoRepository postInfoRepository;

    //Comment + PostInfo
    @Transactional
    public void createComment(long postId, long userId, String content) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("유저를 찾을 수 없습니다." , null)
        );

        // Comment 생성
        Comment comment = Comment.builder()
                .postId(postId)
                .user(user)
                .content(content)
                .build();

        commentRepository.save(comment);

        // PostInfo 댓글 수 +1
        PostInfo postInfo = postInfoRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("게시글 정보를 찾을 수 없습니다." , null)
        );
        postInfo.increaseCommentCount();
        postInfoRepository.save(postInfo);

    }

    // todo: Pagenation 해야함.
    public List<Map<String, Object>> getCommentsByPost(long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @Transactional
    public void updateComment(long commentId, String content, long loginUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!comment.getUser().getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        comment.updateContent(content);
    }

    //Comment + PostInfo
    @Transactional
    public void removeComment(long commentId, long loginUserId) {
        
        //Comment 삭제
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!comment.getUser().getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);


        commentRepository.delete(comment);
        Long postId = comment.getPostId();

        //PostInfo에 댓글 수 -1
        PostInfo postInfo = postInfoRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("게시글 정보를 찾을 수 없습니다." , null)
        );

        postInfo.decreaseCommentCount();
        postInfoRepository.save(postInfo);

    }
}