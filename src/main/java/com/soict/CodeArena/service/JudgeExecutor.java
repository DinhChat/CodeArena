package com.soict.CodeArena.service;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.SUBMISSION_STATUS;
import com.soict.CodeArena.model.Submission;
import com.soict.CodeArena.model.Testcase;
import com.soict.CodeArena.repository.SubmissionRepository;
import com.soict.CodeArena.repository.TestcaseRepository;
import com.soict.CodeArena.request.SubmissionPayload;
import com.soict.CodeArena.request.TestCaseDTO;
import com.soict.CodeArena.response.SandboxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class JudgeExecutor {

    private final SubmissionRepository submissionRepository;
    private final TestcaseRepository testcaseRepository;
    private final RestTemplate restTemplate;

    @Value("${sandbox.url}")
    private String sandboxUrl;

    @Autowired
    public JudgeExecutor(
            SubmissionRepository submissionRepository,
            TestcaseRepository testcaseRepository,
            RestTemplateBuilder builder
    ) {
        this.submissionRepository = submissionRepository;
        this.testcaseRepository = testcaseRepository;
        this.restTemplate = builder.build();
    }

    public void runSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElse(null);

        if (submission == null) return;

        submission.setStatus(SUBMISSION_STATUS.RUNNING);
        submissionRepository.save(submission);

        try {
            Problem problem = submission.getProblem();

            List<Testcase> testcases = testcaseRepository
                    .findByProblem_ProblemIdOrderByOrderIndexAsc(problem.getProblemId());

            List<TestCaseDTO> testcaseDTOs = testcases.stream()
                    .map(tc -> new TestCaseDTO(tc.getInput(), tc.getExpectedOutput()))
                    .toList();

            SubmissionPayload payload = new SubmissionPayload();
            payload.setSubmissionCode(submission.getCode());
            payload.setLanguage(submission.getLanguage());
            payload.setTestcases(testcaseDTOs);
            payload.setTimeLimit(problem.getTimeLimit());
            payload.setMemoryLimit(problem.getMemoryLimit());

            ResponseEntity<SandboxResponse> response = restTemplate.postForEntity(
                    sandboxUrl + "/submissions/run",
                    payload,
                    SandboxResponse.class
            );

            SandboxResponse result = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && result != null) {
                submission.setPassedTestcases(result.getPassed());
                submission.setTotalTestcases(result.getTotal());
                submission.setExecutionTime(result.getExecutionTime().intValue());
                submission.setMemoryUsed(result.getMemoryUsed());
                submission.setErrorMessage(result.getError());
                submission.setStatus(SUBMISSION_STATUS.valueOf(result.getStatus()));
            } else {
                submission.setStatus(SUBMISSION_STATUS.RUNTIME_ERROR);
            }

        } catch (Exception e) {
            submission.setStatus(SUBMISSION_STATUS.RUNTIME_ERROR);
            submission.setErrorMessage(e.getMessage());
        }

        submission.setJudgedAt(LocalDateTime.now());
        submissionRepository.save(submission);
    }
}
