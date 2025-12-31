package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.Testcase;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.repository.TestcaseRepository;
import com.soict.CodeArena.repository.UserRepository;
import com.soict.CodeArena.request.TestcaseRequest;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.TestcaseResponse;
import com.soict.CodeArena.service.TestcaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<TestcaseResponse> createTestcase(Long problemId, List<TestcaseRequest> requests, String username)
            throws ResponseStatusException {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem Not Found"));
        User user = userRepository.findByUsername(username);

        if (!problem.getCreatedBy().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot create Testcase for not your Problem.");
        }

        List<Testcase> testcases = requests.stream()
                .map(req -> {
                    Testcase tc = new Testcase();
                    tc.setProblem(problem);
                    tc.setInput(req.getInput());
                    tc.setExpectedOutput(req.getExpectedOutput());
                    tc.setSample(req.isSample());
                    tc.setOrderIndex(req.getOrderIndex());
                    return tc;
                })
                .toList();

        List<Testcase> savedTestcases = testcaseRepository.saveAll(testcases);
        return savedTestcases.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public TestcaseResponse updateTestcase(Long testcaseId, TestcaseRequest request, String username) throws ResponseStatusException {
        Testcase testcase = testcaseRepository.findById(testcaseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Testcase Not Found"));
        User user = userRepository.findByUsername(username);

        if (!testcase.getProblem().getCreatedBy().equals(user)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Cannot update Testcase for not your Problem.");
        }

        testcase.setInput(request.getInput());
        testcase.setExpectedOutput(request.getExpectedOutput());
        testcase.setSample(request.isSample());
        testcase.setOrderIndex(request.getOrderIndex());

        Testcase updatedTestcase = testcaseRepository.save(testcase);
        return convertToResponse(updatedTestcase);
    }

    @Override
    public PagedResponse<TestcaseResponse> getAllTestcasesByProblem(Long problemId, Integer page, Integer pageSize,
            Integer offset) {
        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("orderIndex").ascending());
        Page<Testcase> testcasePage = testcaseRepository.findByProblem_ProblemId(problemId, pageable);

        List<TestcaseResponse> responses = testcasePage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                testcasePage.getNumber(),
                testcasePage.getSize(),
                testcasePage.getTotalElements(),
                testcasePage.getTotalPages(),
                testcasePage.isLast(),
                testcasePage.isFirst());
    }

    @Override
    public PagedResponse<TestcaseResponse> getSampleTestcasesByProblem(Long problemId, Integer page, Integer pageSize,
            Integer offset) {
        int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int actualPage;

        if (offset != null && offset >= 0) {
            actualPage = offset / actualPageSize;
        } else if (page != null && page >= 0) {
            actualPage = page;
        } else {
            actualPage = 0;
        }

        Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("orderIndex").ascending());
        Page<Testcase> testcasePage = testcaseRepository.findByProblem_ProblemIdAndIsSampleTrue(problemId, pageable);

        List<TestcaseResponse> responses = testcasePage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                testcasePage.getNumber(),
                testcasePage.getSize(),
                testcasePage.getTotalElements(),
                testcasePage.getTotalPages(),
                testcasePage.isLast(),
                testcasePage.isFirst());
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public void deleteTestcase(Long testcaseId, String username) throws ResponseStatusException {
        User user = userRepository.findByUsername(username);
        Testcase testcase = testcaseRepository.findById(testcaseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Testcase Not Found"));

        if (!testcase.getProblem().getCreatedBy().equals(user)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Cannot delete Testcase for not your Problem.");
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
