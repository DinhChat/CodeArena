package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.Submission;
import com.soict.CodeArena.model.TestcaseResult;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.SubmissionRepository;
import com.soict.CodeArena.repository.TestcaseResultRepository;
import com.soict.CodeArena.repository.UserRepository;
import com.soict.CodeArena.response.UITestcaseResultResponse;
import com.soict.CodeArena.service.TestcaseResultService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestcaseResultServiceImpl implements TestcaseResultService {
    private final TestcaseResultRepository testcaseResultRepository;
    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    public TestcaseResultServiceImpl(TestcaseResultRepository testcaseResultRepository, UserRepository userRepository, SubmissionRepository submissionRepository) {
        this.testcaseResultRepository = testcaseResultRepository;
        this.userRepository = userRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public List<UITestcaseResultResponse> getAllTestcaseResult(Long submissionId, String username) throws Exception {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new Exception("username not found");
        }
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new Exception("Submission not found"));
        if (!submission.getCreatedBy().equals(user)) {
            throw new Exception("You do not have permission to view this submission's testcase results");
        }
        List<TestcaseResult> testcaseResults = testcaseResultRepository.findBySubmission_SubmissionIdOrderByTestcaseResultIdAsc(submissionId);

        if (testcaseResults.isEmpty()) {
            return new ArrayList<>();
        }

        return testcaseResults.stream()
                .map(this::mapToUITestcaseResultResponse)
                .collect(Collectors.toList());
    }

    private UITestcaseResultResponse mapToUITestcaseResultResponse(TestcaseResult tr) {
        UITestcaseResultResponse dto = new UITestcaseResultResponse();
        dto.setTestcaseNumber(tr.getTestcaseNumber());
        dto.setInput(tr.getInput());
        dto.setExpectedOutput(tr.getExpectedOutput());
        dto.setActualOutput(tr.getActualOutput());
        dto.setTimeTaken(tr.getTimeTaken());
        dto.setMemoryUsed(tr.getMemoryUsed());
        dto.setStatus(tr.getStatus());
        dto.setPassed(tr.getPassed());

        return dto;
    }
}
