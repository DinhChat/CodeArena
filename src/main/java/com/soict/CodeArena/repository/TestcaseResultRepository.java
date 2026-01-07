package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.TestcaseResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestcaseResultRepository extends JpaRepository<TestcaseResult,Long> {
    List<TestcaseResult> findBySubmission_SubmissionIdOrderByTestcaseResultIdAsc(Long submissionId);
    void deleteBySubmission_SubmissionId(Long submissionId);
}
