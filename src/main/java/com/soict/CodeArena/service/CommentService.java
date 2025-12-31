package com.soict.CodeArena.service;

import com.soict.CodeArena.request.CommentRequest;
import com.soict.CodeArena.response.CommentResponse;
import com.soict.CodeArena.response.PagedResponse;

public interface CommentService {

    CommentResponse createComment(CommentRequest request, String username) throws Exception;

    CommentResponse updateComment(Long commentId, CommentRequest request, String username) throws Exception;

    void deleteComment(Long commentId, String username) throws Exception;

    PagedResponse<CommentResponse> getCommentsByProblem(Long problemId, Integer page, Integer pageSize, Integer offset);

    CommentResponse getCommentById(Long commentId) throws Exception;

    PagedResponse<CommentResponse> getCommentsByUser(Long userId, Integer page, Integer pageSize, Integer offset);

    PagedResponse<CommentResponse> getRepliesByComment(Long commentId, Integer page, Integer pageSize, Integer offset);
}
