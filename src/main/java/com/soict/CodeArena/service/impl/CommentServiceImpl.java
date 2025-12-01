package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.Comment;
import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.CommentRepository;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.request.CommentRequest;
import com.soict.CodeArena.response.CommentResponse;
import com.soict.CodeArena.service.CommentService;
import com.soict.CodeArena.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private UserService userService;

    @Override
    public CommentResponse createComment(CommentRequest request, String username) throws Exception {
        User user = userService.findByUsername(username);

        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new Exception("Problem not found"));

        Comment comment = new Comment();
        comment.setProblem(problem);
        comment.setUser(user);
        comment.setContent(request.getContent());

        if (request.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new Exception("Parent comment not found"));
            comment.setParentComment(parentComment);
        }

        Comment savedComment = commentRepository.save(comment);
        return convertToResponse(savedComment, false); // Don't load replies for new comment
    }

    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest request, String username) throws Exception {
        User user = userService.findByUsername(username);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception("Comment not found"));

        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only update your own comments");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return convertToResponse(updatedComment, true);
    }

    @Override
    public void deleteComment(Long commentId, String username) throws Exception {
        User user = userService.findByUsername(username);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception("Comment not found"));

        if (!comment.getUser().getUserId().equals(user.getUserId())
                && !user.getRole().name().equals("ADMIN")
                && !user.getRole().name().equals("MANAGER")) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    @Override
    public List<CommentResponse> getCommentsByProblem(Long problemId) {
        List<Comment> comments = commentRepository
                .findByProblem_ProblemIdAndIsDeletedFalseAndParentCommentIsNullOrderByCreatedDateDesc(problemId);

        return comments.stream()
                .map(comment -> convertToResponse(comment, true)) // Load replies
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse getCommentById(Long commentId) throws Exception {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception("Comment not found"));

        if (comment.isDeleted()) {
            throw new Exception("Comment has been deleted");
        }

        return convertToResponse(comment, true);
    }

    @Override
    public List<CommentResponse> getCommentsByUser(Long userId) {
        List<Comment> comments = commentRepository
                .findByUser_UserIdAndIsDeletedFalseOrderByCreatedDateDesc(userId);

        return comments.stream()
                .map(comment -> convertToResponse(comment, false))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getRepliesByComment(Long commentId) {
        List<Comment> replies = commentRepository
                .findByParentComment_CommentIdAndIsDeletedFalseOrderByCreatedDateAsc(commentId);

        return replies.stream()
                .map(reply -> convertToResponse(reply, false))
                .collect(Collectors.toList());
    }

    private CommentResponse convertToResponse(Comment comment, boolean includeReplies) {
        CommentResponse response = new CommentResponse();
        response.setCommentId(comment.getCommentId());
        response.setProblemId(comment.getProblem().getProblemId());
        response.setProblemTitle(comment.getProblem().getTitle());
        response.setUserId(comment.getUser().getUserId());
        response.setUsername(comment.getUser().getUsername());
        response.setContent(comment.getContent());
        response.setParentCommentId(
                comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null);
        response.setCreatedDate(comment.getCreatedDate());
        response.setUpdatedDate(comment.getUpdatedDate());

        if (includeReplies) {
            List<CommentResponse> replies = getRepliesByComment(comment.getCommentId());
            response.setReplies(replies);
            response.setReplyCount(replies.size());
        }

        return response;
    }
}
