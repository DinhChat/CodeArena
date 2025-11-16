package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.SubmissionResponse;
import com.soict.CodeArena.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<SubmissionResponse> submitSolution(
            @RequestBody SubmissionRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        SubmissionResponse response = submissionService.submitSolution(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionResponse> getSubmissionById(@PathVariable Long submissionId) throws Exception {
        SubmissionResponse response = submissionService.getSubmissionById(submissionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByUser(
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        List<SubmissionResponse> responses = submissionService.getSubmissionsByUser(username);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByProblem(@PathVariable Long problemId) {
        List<SubmissionResponse> responses = submissionService.getSubmissionsByProblem(problemId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/user/problem/{problemId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByUserAndProblem(
            @PathVariable Long problemId,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        List<SubmissionResponse> responses = submissionService.getSubmissionsByUserAndProblem(username, problemId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
