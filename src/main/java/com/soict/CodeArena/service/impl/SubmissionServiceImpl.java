package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.component.JudgeExecutor;
import com.soict.CodeArena.model.*;
import com.soict.CodeArena.repository.*;
import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.DefaultSubmissionResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.SubmissionDetailResponse;
import com.soict.CodeArena.response.SubmissionItemResponse;
import com.soict.CodeArena.service.SubmissionService;
import com.soict.CodeArena.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        private final JudgeExecutor judgeExecutor;


        public SubmissionServiceImpl(
                        SubmissionRepository submissionRepository,
                        ProblemRepository problemRepository,
                        TestcaseRepository testcaseRepository,
                        UserService userService,
                        UserProblemStatRepository userProblemStatRepository,
                        JudgeExecutor judgeExecutor
                        ) {
                this.submissionRepository = submissionRepository;
                this.problemRepository = problemRepository;
                this.testcaseRepository = testcaseRepository;
                this.userService = userService;
                this.userProblemStatRepository = userProblemStatRepository;
                this.judgeExecutor = judgeExecutor;
        }

        @Override
        public SubmissionDetailResponse submitSolution(SubmissionRequest request, String username) throws ResponseStatusException {
                User user = userService.findByUsername(username);
                Problem problem = problemRepository.findById(request.getProblemId())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Problem Not Found"));

                Submission submission = new Submission();
                submission.setProblem(problem);
                submission.setCreatedBy(user);
                submission.setCode(request.getCode());
                submission.setLanguage(request.getLanguage().toUpperCase());
                submission.setStatus(SUBMISSION_STATUS.RUNNING);
                submission.setSubmittedAt(LocalDateTime.now());

                List<Testcase> testcases = testcaseRepository
                                .findByProblem_ProblemIdOrderByOrderIndexAsc(problem.getProblemId());
                submission.setTotalTestcases(testcases.size());
                submission.setPassedTestcases(0);

                Submission savedSubmission = submissionRepository.save(submission);
                judgeExecutor.runSubmission(savedSubmission.getSubmissionId());
                return convertToResponse(savedSubmission);
        }

        @Override
        public SubmissionDetailResponse getSubmissionById(Long submissionId, String username) throws ResponseStatusException {
                User user = userService.findByUsername(username);
                Submission submission = submissionRepository.findById(submissionId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Submission Not Found"));

                if (!submission.getCreatedBy().equals(user))
                        throw new AccessDeniedException("Can't get other submission");
                return convertToResponse(submission);
        }

        @Override
        public PagedResponse<DefaultSubmissionResponse> getMySubmissions(String username, Integer page,
                        Integer pageSize, Integer offset) throws ResponseStatusException {
                User user = userService.findByUsername(username);
                int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
                int actualPage;

                if (offset != null && offset >= 0) {
                        actualPage = offset / actualPageSize;
                } else if (page != null && page >= 0) {
                        actualPage = page;
                } else {
                        actualPage = 0;
                }

                Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("submittedAt").descending());
                Page<Submission> submissionPage = submissionRepository
                                .findByCreatedBy_UserId(user.getUserId(), pageable);

                List<DefaultSubmissionResponse> responses = submissionPage.getContent().stream()
                                .map(this::convertToDefaultResponse)
                                .collect(Collectors.toList());

                return new PagedResponse<>(
                                responses,
                                submissionPage.getNumber(),
                                submissionPage.getSize(),
                                submissionPage.getTotalElements(),
                                submissionPage.getTotalPages(),
                                submissionPage.isLast(),
                                submissionPage.isFirst());
        }

        @Override
        public PagedResponse<SubmissionItemResponse> getSubmissionsByUserAndProblem(String username, Long problemId,
                        Integer page, Integer pageSize, Integer offset) throws ResponseStatusException {
                User user = userService.findByUsername(username);

                int actualPageSize = (pageSize != null && pageSize > 0) ? pageSize : 10;
                int actualPage;

                if (offset != null && offset >= 0) {
                        actualPage = offset / actualPageSize;
                } else if (page != null && page >= 0) {
                        actualPage = page;
                } else {
                        actualPage = 0;
                }

                Pageable pageable = PageRequest.of(actualPage, actualPageSize, Sort.by("submittedAt").descending());
                Page<Submission> submissionPage = submissionRepository
                                .findByCreatedBy_UserIdAndProblem_ProblemId(user.getUserId(), problemId, pageable);

                Problem problem = problemRepository.findById(problemId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Problem Not Found"));

                Long bestSubmissionId = userProblemStatRepository
                                .findByUserAndProblem(user, problem)
                                .map(stat -> stat.getSubmission().getSubmissionId())
                                .orElse(null);

                List<SubmissionItemResponse> responses = submissionPage.getContent().stream()
                                .map(sub -> convertToItemResponse(sub, bestSubmissionId))
                                .collect(Collectors.toList());

                return new PagedResponse<>(
                                responses,
                                submissionPage.getNumber(),
                                submissionPage.getSize(),
                                submissionPage.getTotalElements(),
                                submissionPage.getTotalPages(),
                                submissionPage.isLast(),
                                submissionPage.isFirst());
        }

        @Override
        public void deleteSubmission(Long submissionId) {
                Submission submission = submissionRepository.findById(submissionId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Submission Not Found"));
                userProblemStatRepository.deleteBySubmission_SubmissionId(submissionId);
                submissionRepository.delete(submission);
        }

        private SubmissionDetailResponse convertToResponse(Submission submission) {
                SubmissionDetailResponse response = new SubmissionDetailResponse();
                response.setSubmissionId(submission.getSubmissionId());
                response.setProblemCode(submission.getProblem().getProblemCode());
                response.setProblemTitle(submission.getProblem().getTitle());
                response.setUsername(submission.getCreatedBy().getUsername());
                response.setLanguage(submission.getLanguage());
                response.setCode(submission.getCode());
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
                                submission.getSubmissionId().equals(bestSubmissionId));
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
