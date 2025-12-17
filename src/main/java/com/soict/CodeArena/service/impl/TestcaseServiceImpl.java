package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.Testcase;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.repository.TestcaseRepository;
import com.soict.CodeArena.repository.UserRepository;
import com.soict.CodeArena.request.TestcaseRequest;
import com.soict.CodeArena.response.TestcaseResponse;
import com.soict.CodeArena.service.TestcaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestcaseServiceImpl implements TestcaseService {
    private final TestcaseRepository testcaseRepository;
    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;

    public TestcaseServiceImpl(
            TestcaseRepository testcaseRepository,
            ProblemRepository problemRepository,
            UserRepository userRepository) {
        this.testcaseRepository = testcaseRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public TestcaseResponse createTestcase(Long problemId, TestcaseRequest request, String username) throws Exception {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new Exception("Problem not found"));
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }

        if (!problem.getCreatedBy().equals(user)) {
            throw new Exception("Cannot create testcase");
        }

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
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!testcase.getProblem().getCreatedBy().equals(user)) {
            throw new Exception("Cannot update testcase");
        }

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
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        Testcase testcase = testcaseRepository.findById(testcaseId)
                .orElseThrow(() -> new Exception("Testcase not found"));

        if (!testcase.getProblem().getCreatedBy().equals(user)) {
            throw new Exception("Cannot delete testcase");
        }
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
