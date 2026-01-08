package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.ProblemRequest;
import com.soict.CodeArena.response.DefaultProblemResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.ProblemItemResponse;
import com.soict.CodeArena.response.ProblemDetailResponse;
import com.soict.CodeArena.service.ProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @PostMapping
    public ResponseEntity<ProblemDetailResponse> createProblem(
            @RequestBody ProblemRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        ProblemDetailResponse response = problemService.createProblem(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<ProblemDetailResponse> updateProblem(
            @PathVariable Long problemId,
            @RequestBody ProblemRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        ProblemDetailResponse response = problemService.updateProblem(problemId, request, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemDetailResponse> getProblemById(@PathVariable Long problemId) throws Exception {
        ProblemDetailResponse response = problemService.getProblemById(problemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{problemCode}")
    public ResponseEntity<ProblemDetailResponse> getProblemByCode(@PathVariable String problemCode) throws Exception {
        ProblemDetailResponse response = problemService.getProblemByCode(problemCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<PagedResponse<DefaultProblemResponse>> getMyProblems(
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) throws Exception {
        String username = authentication.getName();
        PagedResponse<DefaultProblemResponse> responses = problemService.getMyProblems(username, page, pageSize,
                offset);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ProblemItemResponse>> getActiveProblems(
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) throws Exception {
        String username = authentication.getName();
        PagedResponse<ProblemItemResponse> responses = problemService.getActiveProblems(username, page, pageSize,
                offset);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/toggle-active/{problemId}")
    public ResponseEntity<ProblemDetailResponse> activateProblem(
            @PathVariable Long problemId,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        ProblemDetailResponse problemDetailResponse = problemService.toggleActiveProblem(problemId, username);
        return new ResponseEntity<>(problemDetailResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{problemId}")
    public ResponseEntity<String> deleteProblem(
            @PathVariable Long problemId,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        problemService.deleteProblem(problemId, username);
        return new ResponseEntity<>("Problem deleted successfully", HttpStatus.OK);
    }
}
