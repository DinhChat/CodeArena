package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.Testcase;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.repository.TestcaseRepository;
import com.soict.CodeArena.request.TestcaseRequest;
import com.soict.CodeArena.response.TestcaseResponse;
import com.soict.CodeArena.service.TestcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestcaseServiceImpl implements TestcaseService {

    @Autowired
    private TestcaseRepository testcaseRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public TestcaseResponse createTestcase(Long problemId, TestcaseRequest request, String username) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new Exception("Problem not found"));

        Testcase testcase = new Testcase();
        testcase.setProblem(problem);
        testcase.setInput(request.getInput());
        testcase.setExpectedOutput(request.getExpectedOutput());
        testcase.setSample(request.isSample());
        testcase.setOrderIndex(request.getOrderIndex());

        Testcase savedTestcase = testcaseRepository.save(testcase);
        return convertToResponse(savedTestcase);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public TestcaseResponse updateTestcase(Long testcaseId, TestcaseRequest request, String username) throws Exception {
        Testcase testcase = testcaseRepository.findById(testcaseId)
                .orElseThrow(() -> new Exception("Testcase not found"));

        testcase.setInput(request.getInput());
        testcase.setExpectedOutput(request.getExpectedOutput());
        testcase.setSample(request.isSample());
        testcase.setOrderIndex(request.getOrderIndex());

        Testcase updatedTestcase = testcaseRepository.save(testcase);
        return convertToResponse(updatedTestcase);
    }

    @Override
    public List<TestcaseResponse> getAllTestcasesByProblem(Long problemId) {
        return testcaseRepository.findByProblem_ProblemIdOrderByOrderIndexAsc(problemId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestcaseResponse> getSampleTestcasesByProblem(Long problemId) {
        return testcaseRepository.findByProblem_ProblemIdAndIsSampleTrue(problemId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public void deleteTestcase(Long testcaseId, String username) throws Exception {
        Testcase testcase = testcaseRepository.findById(testcaseId)
                .orElseThrow(() -> new Exception("Testcase not found"));
        testcaseRepository.delete(testcase);
    }

    private TestcaseResponse convertToResponse(Testcase testcase) {
        TestcaseResponse response = new TestcaseResponse();
        response.setTestcaseId(testcase.getTestcaseId());
        response.setInput(testcase.getInput());
        response.setExpectedOutput(testcase.getExpectedOutput());
        response.setSample(testcase.isSample());
        response.setOrderIndex(testcase.getOrderIndex());
        return response;
    }
}
