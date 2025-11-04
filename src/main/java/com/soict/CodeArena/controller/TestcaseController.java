package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.TestcaseRequest;
import com.soict.CodeArena.response.TestcaseResponse;
import com.soict.CodeArena.service.TestcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/testcases")
public class TestcaseController {

    @Autowired
    private TestcaseService testcaseService;

    @PostMapping("/problem/{problemId}")
    public ResponseEntity<TestcaseResponse> createTestcase(
            @PathVariable Long problemId,
            @RequestBody TestcaseRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        TestcaseResponse response = testcaseService.createTestcase(problemId, request, username);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{testcaseId}")
    public ResponseEntity<TestcaseResponse> updateTestcase(
            @PathVariable Long testcaseId,
            @RequestBody TestcaseRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        TestcaseResponse response = testcaseService.updateTestcase(testcaseId, request, username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<TestcaseResponse>> getAllTestcasesByProblem(@PathVariable Long problemId) {
        List<TestcaseResponse> responses = testcaseService.getAllTestcasesByProblem(problemId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/problem/{problemId}/samples")
    public ResponseEntity<List<TestcaseResponse>> getSampleTestcasesByProblem(@PathVariable Long problemId) {
        List<TestcaseResponse> responses = testcaseService.getSampleTestcasesByProblem(problemId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/{testcaseId}")
    public ResponseEntity<String> deleteTestcase(
            @PathVariable Long testcaseId,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        testcaseService.deleteTestcase(testcaseId, username);
        return new ResponseEntity<>("Testcase deleted successfully", HttpStatus.OK);
    }
}
