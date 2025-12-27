package com.soict.CodeArena.service;

import com.soict.CodeArena.request.TestcaseRequest;
import com.soict.CodeArena.response.TestcaseResponse;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface TestcaseService {
    List<TestcaseResponse> createTestcase(Long problemId, List<TestcaseRequest> requests, String username) throws ResponseStatusException;

    TestcaseResponse updateTestcase(Long testcaseId, TestcaseRequest request, String username) throws ResponseStatusException;

    List<TestcaseResponse> getAllTestcasesByProblem(Long problemId);

    List<TestcaseResponse> getSampleTestcasesByProblem(Long problemId);

    void deleteTestcase(Long testcaseId, String username) throws ResponseStatusException;
}
