package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.*;
import com.soict.CodeArena.repository.ProblemRepository;
import com.soict.CodeArena.repository.SubmissionRepository;
import com.soict.CodeArena.repository.TestcaseRepository;
import com.soict.CodeArena.repository.UserProblemStatRepository;
import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.DefaultSubmissionResponse;
import com.soict.CodeArena.response.SubmissionDetailResponse;
import com.soict.CodeArena.component.SubmissionQueue;
import com.soict.CodeArena.response.SubmissionItemResponse;
import com.soict.CodeArena.service.SubmissionService;
import com.soict.CodeArena.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final TestcaseRepository testcaseRepository;
    private final UserService userService;
    private final UserProblemStatRepository userProblemStatRepository;
    private final SubmissionQueue  submissionQueue;

    public SubmissionServiceImpl(
            SubmissionRepository submissionRepository,
            ProblemRepository problemRepository,
            TestcaseRepository testcaseRepository,
            UserService userService,
            UserProblemStatRepository userProblemStatRepository,
            SubmissionQueue  submissionQueue
    ) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.testcaseRepository = testcaseRepository;
        this.userService = userService;
        this.userProblemStatRepository = userProblemStatRepository;
        this.submissionQueue = submissionQueue;
    }

    @Override
    public SubmissionDetailResponse submitSolution(SubmissionRequest request, String username) throws Exception {
        User user = userService.findByUsername(username);
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Problem Not Found"
                ));

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
    public SubmissionDetailResponse getSubmissionById(Long submissionId, String username) throws Exception {
        User  user = userService.findByUsername(username);
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission Not Found"));

        if (!submission.getCreatedBy().equals(user)) throw new AccessDeniedException("Can't get other submission");
        return convertToResponse(submission);
    }

    @Override
    public List<DefaultSubmissionResponse> getMySubmissions(String username) throws Exception {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalStateException("Authenticated user not found");
        }

        return submissionRepository
                .findByCreatedBy_UserIdOrderBySubmittedAtDesc(user.getUserId())
                .stream()
                .map(this::convertToDefaultResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionItemResponse> getSubmissionsByUserAndProblem(String username, Long problemId) throws Exception {
        User user = userService.findByUsername(username);
        List<Submission> submissions =
                submissionRepository
                        .findByCreatedBy_UserIdAndProblem_ProblemIdOrderBySubmittedAtDesc(
                                user.getUserId(), problemId
                        );

        Long bestSubmissionId = userProblemStatRepository
                .findByUserAndProblem(user, submissions.getFirst().getProblem())
                .map(stat -> stat.getSubmission().getSubmissionId())
                .orElse(null);

        return submissions.stream()
                .map(sub -> convertToItemResponse(sub, bestSubmissionId))
                .toList();
    }

    private SubmissionDetailResponse convertToResponse(Submission submission) {
        SubmissionDetailResponse response = new SubmissionDetailResponse();
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

    private SubmissionItemResponse convertToItemResponse(
            Submission submission,
            Long bestSubmissionId) {

        return new SubmissionItemResponse(
                submission.getSubmissionId(),
                submission.getStatus(),
                submission.getPassedTestcases(),
                submission.getTotalTestcases(),
                submission.getExecutionTime(),
                submission.getMemoryUsed(),
                submission.getSubmissionId().equals(bestSubmissionId)
        );
    }

    private DefaultSubmissionResponse convertToDefaultResponse(
            Submission submission) {

        DefaultSubmissionResponse res = new DefaultSubmissionResponse();

        res.setSubmissionId(submission.getSubmissionId());
        res.setProblemTitle(submission.getProblem().getTitle());
        res.setLanguage(submission.getLanguage());
        res.setStatus(submission.getStatus());
        res.setPassedTestcases(submission.getPassedTestcases());
        res.setTotalTestcases(submission.getTotalTestcases());
        res.setJudgedAt(submission.getJudgedAt());

        return res;
    }
}
