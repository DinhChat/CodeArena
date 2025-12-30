package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.Comment;
import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.CommentRepository;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.request.CommentRequest;
import com.soict.CodeArena.response.CommentResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.service.CommentService;
import com.soict.CodeArena.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PagedResponse<CommentResponse> getCommentsByProblem(Long problemId, Integer page, Integer pageSize,
            Integer offset) {
        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("createdDate").descending());
        Page<Comment> commentPage = commentRepository
                .findByProblem_ProblemIdAndIsDeletedFalseAndParentCommentIsNull(problemId, pageable);

        List<CommentResponse> responses = commentPage.getContent().stream()
                .map(comment -> convertToResponse(comment, true))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                commentPage.getNumber(),
                commentPage.getSize(),
                commentPage.getTotalElements(),
                commentPage.getTotalPages(),
                commentPage.isLast(),
                commentPage.isFirst());
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
    public PagedResponse<CommentResponse> getCommentsByUser(Long userId, Integer page, Integer pageSize,
            Integer offset) {
        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("createdDate").descending());
        Page<Comment> commentPage = commentRepository
                .findByUser_UserIdAndIsDeletedFalse(userId, pageable);

        List<CommentResponse> responses = commentPage.getContent().stream()
                .map(comment -> convertToResponse(comment, false))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                commentPage.getNumber(),
                commentPage.getSize(),
                commentPage.getTotalElements(),
                commentPage.getTotalPages(),
                commentPage.isLast(),
                commentPage.isFirst());
    }

    @Override
    public PagedResponse<CommentResponse> getRepliesByComment(Long commentId, Integer page, Integer pageSize,
            Integer offset) {
        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("createdDate").ascending());
        Page<Comment> commentPage = commentRepository
                .findByParentComment_CommentIdAndIsDeletedFalse(commentId, pageable);

        List<CommentResponse> responses = commentPage.getContent().stream()
                .map(reply -> convertToResponse(reply, false))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                commentPage.getNumber(),
                commentPage.getSize(),
                commentPage.getTotalElements(),
                commentPage.getTotalPages(),
                commentPage.isLast(),
                commentPage.isFirst());
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
            List<CommentResponse> replies = getRepliesByComment(comment.getCommentId(), null, null, null)
                    .getContent();
            response.setReplies(replies);
            response.setReplyCount(replies.size());
        }

        return response;
    }
}
