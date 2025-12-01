package com.soict.CodeArena.service;

import com.soict.CodeArena.request.CommentRequest;
import com.soict.CodeArena.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(CommentRequest request, String username) throws Exception;

    CommentResponse updateComment(Long commentId, CommentRequest request, String username) throws Exception;

    void deleteComment(Long commentId, String username) throws Exception;

    List<CommentResponse> getCommentsByProblem(Long problemId);

    CommentResponse getCommentById(Long commentId) throws Exception;

    List<CommentResponse> getCommentsByUser(Long userId);

    List<CommentResponse> getRepliesByComment(Long commentId);
}
