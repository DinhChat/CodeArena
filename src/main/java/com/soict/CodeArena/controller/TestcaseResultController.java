package com.soict.CodeArena.controller;

import com.soict.CodeArena.response.UITestcaseResultResponse;
import com.soict.CodeArena.service.TestcaseResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/result")
public class TestcaseResultController {
    private final TestcaseResultService testcaseResultService;

    @Autowired
    public TestcaseResultController(TestcaseResultService testcaseResultService) {
        this.testcaseResultService = testcaseResultService;
    }

    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<List<UITestcaseResultResponse>> getTestcaseResult(
            @PathVariable Long submissionId,
            Authentication authentication
    ) throws Exception {
        String username = authentication.getName();
        List<UITestcaseResultResponse> UITestcaseResultResponseList = testcaseResultService.getAllTestcaseResult(submissionId, username);
        return new  ResponseEntity<>(UITestcaseResultResponseList, HttpStatus.OK);
    }
}
