package com.soict.CodeArena.controller;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import com.soict.CodeArena.request.ProblemRequest;
import com.soict.CodeArena.response.ProblemResponse;
import com.soict.CodeArena.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/problems")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @PostMapping
    public ResponseEntity<ProblemResponse> createProblem(
            @RequestBody ProblemRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        ProblemResponse response = problemService.createProblem(request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{problemId}")
    public ResponseEntity<ProblemResponse> updateProblem(
            @PathVariable Long problemId,
            @RequestBody ProblemRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        ProblemResponse response = problemService.updateProblem(problemId, request, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponse> getProblemById(@PathVariable Long problemId) throws Exception {
        ProblemResponse response = problemService.getProblemById(problemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{problemCode}")
    public ResponseEntity<ProblemResponse> getProblemByCode(@PathVariable String problemCode) throws Exception {
        ProblemResponse response = problemService.getProblemByCode(problemCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProblemResponse>> getAllProblems() {
        List<ProblemResponse> responses = problemService.getAllProblems();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProblemResponse>> getActiveProblems() {
        List<ProblemResponse> responses = problemService.getActiveProblems();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<ProblemResponse>> getProblemsByDifficulty(
            @PathVariable DIFFICULTY_LEVEL difficulty) {
        List<ProblemResponse> responses = problemService.getProblemsByDifficulty(difficulty);
        return new ResponseEntity<>(responses, HttpStatus.OK);
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
