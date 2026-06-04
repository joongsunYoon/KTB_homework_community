package com.example.community.domain.comment;

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

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void createComment(int postId, int userId, String content) {
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(content)
                .build();
        commentRepository.save(comment);
    }

    public List<Map<String, Object>> getCommentsByPost(int postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @Transactional
    public void updateComment(int commentId, String content, int loginUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!comment.getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        comment.updateContent(content);
    }

    @Transactional
    public void removeComment(int commentId, int loginUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("해당 내용을 찾을 수 없습니다.", null));

        if (!comment.getUserId().equals(loginUserId)) throw new ForbiddenException("사용자 권한이 없습니다.", null);

        commentRepository.delete(comment);
    }
}