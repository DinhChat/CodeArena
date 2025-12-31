package com.soict.CodeArena.controller;

import com.soict.CodeArena.request.CreateTestcasesRequest;
import com.soict.CodeArena.request.TestcaseRequest;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.TestcaseResponse;
import com.soict.CodeArena.service.TestcaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/testcases")
public class TestcaseController {

    private final TestcaseService testcaseService;

    public TestcaseController(TestcaseService testcaseService) {
        this.testcaseService = testcaseService;
    }

    @PostMapping("/problem/{problemId}")
    public ResponseEntity<List<TestcaseResponse>> createTestcase(
            @PathVariable Long problemId,
            @RequestBody CreateTestcasesRequest request,
            Authentication authentication) throws Exception {
        String username = authentication.getName();
        List<TestcaseResponse> response = testcaseService.createTestcase(problemId, request.getTestcases(), username);
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
    public ResponseEntity<PagedResponse<TestcaseResponse>> getAllTestcasesByProblem(
            @PathVariable Long problemId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) {
        PagedResponse<TestcaseResponse> responses = testcaseService.getAllTestcasesByProblem(problemId, page, pageSize,
                offset);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/problem/{problemId}/samples")
    public ResponseEntity<PagedResponse<TestcaseResponse>> getSampleTestcasesByProblem(
            @PathVariable Long problemId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer offset) {
        PagedResponse<TestcaseResponse> responses = testcaseService.getSampleTestcasesByProblem(problemId, page,
                pageSize, offset);
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
