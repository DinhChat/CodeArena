package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.DefaultSubmissionResponse;
import com.soict.CodeArena.response.SubmissionDetailResponse;
import com.soict.CodeArena.response.SubmissionItemResponse;
import com.soict.CodeArena.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<SubmissionDetailResponse> submitSolution(
            @RequestBody SubmissionRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        SubmissionDetailResponse response = submissionService.submitSolution(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<SubmissionDetailResponse> getSubmissionById(
            @PathVariable Long submissionId,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        SubmissionDetailResponse response = submissionService.getSubmissionById(submissionId, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<DefaultSubmissionResponse>> getAllMySubmissions(
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        List<DefaultSubmissionResponse> responses = submissionService.getMySubmissions(username);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/user/problem/{problemId}")
    public ResponseEntity<List<SubmissionItemResponse>> getSubmissionsByUserAndProblem(
            @PathVariable Long problemId,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        List<SubmissionItemResponse> responses = submissionService.getSubmissionsByUserAndProblem(username, problemId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
