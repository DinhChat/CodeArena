package com.soict.CodeArena.service;

import com.soict.CodeArena.request.TestcaseRequest;
import com.soict.CodeArena.response.TestcaseResponse;

import java.util.List;

public interface TestcaseService {
    List<TestcaseResponse> createTestcase(Long problemId, TestcaseRequest request, String username) throws Exception;

    TestcaseResponse updateTestcase(Long testcaseId, TestcaseRequest request, String username) throws Exception;

    List<TestcaseResponse> getAllTestcasesByProblem(Long problemId);

    List<TestcaseResponse> getSampleTestcasesByProblem(Long problemId);

    void deleteTestcase(Long testcaseId, String username) throws Exception;
}
