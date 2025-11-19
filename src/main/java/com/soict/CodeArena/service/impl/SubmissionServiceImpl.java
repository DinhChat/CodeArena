package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.*;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.repository.SubmissionRepository;
import com.soict.CodeArena.repository.TestcaseRepository;
import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.SubmissionResponse;
import com.soict.CodeArena.service.SubmissionQueue;
import com.soict.CodeArena.service.SubmissionService;
import com.soict.CodeArena.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final TestcaseRepository testcaseRepository;
    private final UserService userService;
    private final SubmissionQueue  submissionQueue;

    public SubmissionServiceImpl(
            SubmissionRepository submissionRepository,
            ProblemRepository problemRepository,
            TestcaseRepository testcaseRepository,
            UserService userService,
            SubmissionQueue  submissionQueue
    ) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.testcaseRepository = testcaseRepository;
        this.userService = userService;
        this.submissionQueue = submissionQueue;
    }

    @Override
    public SubmissionResponse submitSolution(SubmissionRequest request, String username) throws Exception {
        User user = userService.findByUsername(username);
        Problem problem = problemRepository.findByProblemId(request.getProblemId())
                .orElseThrow(() -> new Exception("Problem not found"));

        Submission submission = new Submission();
        submission.setProblem(problem);
        submission.setCreatedBy(user);
        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage().toUpperCase());
        submission.setStatus(SUBMISSION_STATUS.PENDING);
        submission.setSubmittedAt(LocalDateTime.now());

        List<Testcase> testcases = testcaseRepository
                .findByProblem_ProblemIdOrderByOrderIndexAsc(problem.getProblemId());
        submission.setTotalTestcases(testcases.size());
        submission.setPassedTestcases(0);

        Submission savedSubmission = submissionRepository.save(submission);
        submissionQueue.addSubmission(savedSubmission.getSubmissionId());
        return convertToResponse(savedSubmission);
    }

    @Override
    public SubmissionResponse getSubmissionById(Long submissionId) throws Exception {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new Exception("Submission not found"));
        return convertToResponse(submission);
    }

    @Override
    public List<SubmissionResponse> getSubmissionsByUser(String username) throws Exception {
        User user = userService.findByUsername(username);
        return submissionRepository.findByCreatedBy_UserIdOrderBySubmittedAtDesc(user.getUserId()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionResponse> getSubmissionsByProblem(Long problemId) {
        return submissionRepository.findByProblem_ProblemIdOrderBySubmittedAtDesc(problemId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionResponse> getSubmissionsByUserAndProblem(String username, Long problemId) throws Exception {
        User user = userService.findByUsername(username);
        return submissionRepository.findByCreatedBy_UserIdAndProblem_ProblemIdOrderBySubmittedAtDesc(user.getUserId(), problemId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SubmissionResponse convertToResponse(Submission submission) {
        SubmissionResponse response = new SubmissionResponse();
        response.setSubmissionId(submission.getSubmissionId());
        response.setProblemCode(submission.getProblem().getProblemCode());
        response.setProblemTitle(submission.getProblem().getTitle());
        response.setUsername(submission.getCreatedBy().getUsername());
        response.setLanguage(submission.getLanguage());
        response.setStatus(submission.getStatus());
        response.setExecutionTime(submission.getExecutionTime());
        response.setMemoryUsed(submission.getMemoryUsed());
        response.setErrorMessage(submission.getErrorMessage());
        response.setPassedTestcases(submission.getPassedTestcases());
        response.setTotalTestcases(submission.getTotalTestcases());
        response.setSubmittedAt(submission.getSubmittedAt());
        response.setJudgedAt(submission.getJudgedAt());
        return response;
    }
}
