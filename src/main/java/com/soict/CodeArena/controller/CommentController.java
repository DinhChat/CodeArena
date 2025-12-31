package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.CommentRequest;
import com.soict.CodeArena.response.CommentResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @RequestBody CommentRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        CommentResponse response = commentService.createComment(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        CommentResponse response = commentService.updateComment(commentId, request, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        commentService.deleteComment(commentId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<PagedResponse<CommentResponse>> getCommentsByProblem(
            @PathVariable Long problemId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) {
        PagedResponse<CommentResponse> comments = commentService.getCommentsByProblem(problemId, page, pageSize,
                offset);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(
            @PathVariable Long commentId) throws Exception {
        CommentResponse response = commentService.getCommentById(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PagedResponse<CommentResponse>> getCommentsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) {
        PagedResponse<CommentResponse> comments = commentService.getCommentsByUser(userId, page, pageSize, offset);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<PagedResponse<CommentResponse>> getRepliesByComment(
            @PathVariable Long commentId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) {
        PagedResponse<CommentResponse> replies = commentService.getRepliesByComment(commentId, page, pageSize, offset);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }
}
