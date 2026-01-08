package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.DefaultSubmissionResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.SubmissionDetailResponse;
import com.soict.CodeArena.response.SubmissionItemResponse;
import com.soict.CodeArena.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<PagedResponse<DefaultSubmissionResponse>> getAllMySubmissions(
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) throws ResponseStatusException {
        String username = authentication.getName();
        PagedResponse<DefaultSubmissionResponse> responses = submissionService.getMySubmissions(username, page,
                pageSize, offset);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/user/problem/{problemId}")
    public ResponseEntity<PagedResponse<SubmissionItemResponse>> getSubmissionsByUserAndProblem(
            @PathVariable Long problemId,
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) throws ResponseStatusException {
        String username = authentication.getName();
        PagedResponse<SubmissionItemResponse> responses = submissionService.getSubmissionsByUserAndProblem(username,
                problemId, page, pageSize, offset);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
