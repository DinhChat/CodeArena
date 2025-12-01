package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.CommentRequest;
import com.soict.CodeArena.response.CommentResponse;
import com.soict.CodeArena.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CommentResponse>> getCommentsByProblem(
            @PathVariable Long problemId) {
        List<CommentResponse> comments = commentService.getCommentsByProblem(problemId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(
            @PathVariable Long commentId) throws Exception {
        CommentResponse response = commentService.getCommentById(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUser(
            @PathVariable Long userId) {
        List<CommentResponse> comments = commentService.getCommentsByUser(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<CommentResponse>> getRepliesByComment(
            @PathVariable Long commentId) {
        List<CommentResponse> replies = commentService.getRepliesByComment(commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }
}
