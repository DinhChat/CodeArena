package com.soict.CodeArena.component;

import com.soict.CodeArena.model.*;
import com.soict.CodeArena.repository.SubmissionRepository;
import com.soict.CodeArena.repository.TestcaseRepository;
import com.soict.CodeArena.repository.TestcaseResultRepository;
import com.soict.CodeArena.request.SubmissionPayload;
import com.soict.CodeArena.request.TestCaseDTO;
import com.soict.CodeArena.response.SandboxResponse;
import com.soict.CodeArena.service.UserProblemStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JudgeExecutor {
    private final SubmissionRepository submissionRepository;
    private final TestcaseRepository testcaseRepository;
    private final TestcaseResultRepository testcaseResultRepository;
    private final UserProblemStatService userProblemStatService;
    private final RestTemplate restTemplate;

    @Value("${sandbox.url}")
    private String sandboxUrl;

    @Autowired
    public JudgeExecutor(
            SubmissionRepository submissionRepository,
            TestcaseRepository testcaseRepository,
            TestcaseResultRepository testcaseResultRepository,
            UserProblemStatService userProblemStatService,
            RestTemplateBuilder builder) {
        this.submissionRepository = submissionRepository;
        this.testcaseRepository = testcaseRepository;
        this.testcaseResultRepository = testcaseResultRepository;
        this.userProblemStatService = userProblemStatService;
        this.restTemplate = builder.build();
    }

    private TestcaseResult createTestcaseResult(
            SandboxResponse.TestcaseResultResponse res,
            Submission submission) {
        TestcaseResult testcaseResult = new TestcaseResult();
        testcaseResult.setSubmission(submission);
        testcaseResult.setTestcaseNumber(res.getTestCaseNumber());
        testcaseResult.setInput(res.getInput());
        testcaseResult.setExpectedOutput(res.getExpectedOutput());
        testcaseResult.setActualOutput(res.getActualOutput());
        testcaseResult.setTimeTaken(res.getTimeTaken());
        testcaseResult.setMemoryUsed(res.getMemoryUsed());
        testcaseResult.setStatus(res.getStatus());
        testcaseResult.setPassed(res.getPassed());
        return testcaseResult;
    }

    @Async
    @Transactional
    public void runSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow();

        submission.setStatus(SUBMISSION_STATUS.RUNNING);
        submissionRepository.saveAndFlush(submission);

        List<TestcaseResult> testcaseResults = new ArrayList<>();

        try {
            Problem problem = submission.getProblem();

            List<Testcase> testcases = testcaseRepository
                    .findByProblem_ProblemIdOrderByOrderIndexAsc(problem.getProblemId());

            List<TestCaseDTO> testcaseDTOs = testcases.stream()
                    .map(tc -> new TestCaseDTO(tc.getInput(), tc.getExpectedOutput()))
                    .toList();

            SubmissionPayload payload = new SubmissionPayload();
            payload.setSubmissionCode(submission.getCode());
            payload.setLanguage(submission.getLanguage().toLowerCase());
            payload.setTestcases(testcaseDTOs);
            payload.setTimeLimit(problem.getTimeLimit());
            payload.setMemoryLimit(problem.getMemoryLimit());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SubmissionPayload> entity = new HttpEntity<>(payload, headers);

            ResponseEntity<SandboxResponse> response = restTemplate.exchange(
                    sandboxUrl + "/submissions/run",
                    HttpMethod.POST,
                    entity,
                    SandboxResponse.class);

            SandboxResponse sandboxResponse = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && sandboxResponse != null) {
                SandboxResponse.Meta meta = sandboxResponse.getMeta();
                List<SandboxResponse.TestcaseResultResponse> results = sandboxResponse.getResults();

                submission.setPassedTestcases(meta.getPassedTestCases());
                submission.setTotalTestcases(meta.getTotalTestCases());

                for (SandboxResponse.TestcaseResultResponse res : results) {
                    testcaseResults.add(createTestcaseResult(res, submission));
                }

                submission.setExecutionTime(
                        results.stream()
                                .mapToDouble(r -> r.getTimeTaken() != null ? r.getTimeTaken() : 0)
                                .sum());

                submission.setMemoryUsed(
                        results.stream()
                                .mapToInt(r -> r.getMemoryUsed() != null ? r.getMemoryUsed() : 0)
                                .max()
                                .orElse(0));

                submission.setStatus(
                        meta.getAllPassed()
                                ? SUBMISSION_STATUS.ACCEPTED
                                : SUBMISSION_STATUS.WRONG_ANSWER);

            } else {
                submission.setStatus(SUBMISSION_STATUS.RUNTIME_ERROR);
            }

        } catch (Exception e) {
            submission.setStatus(SUBMISSION_STATUS.RUNTIME_ERROR);
            submission.setErrorMessage(e.getMessage());
        }

        submission.setJudgedAt(LocalDateTime.now());
        submissionRepository.saveAndFlush(submission);

        testcaseResultRepository.saveAllAndFlush(testcaseResults);

        userProblemStatService.updateStatAfterJudging(submission);
    }
}
