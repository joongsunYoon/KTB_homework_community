package com.example.community.domain.comment;

import com.example.community.domain.user.entity.User;
import com.example.community.domain.user.repository.UserRepository;
import com.example.community.global.exception.ForbiddenException;
import com.example.community.global.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(
            CommentRepository commentRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createComment(long postId, long userId, String content) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("유저를 찾을 수 없습니다." , null)
        );

        Comment comment = Comment.builder()
                .postId(postId)
                .user(user)
                .content(content)
                .build();
        commentRepository.save(comment);
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

    @Transactional
    public void removeComment(long commentId, long loginUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!comment.getUser().getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        commentRepository.delete(comment);
    }
}