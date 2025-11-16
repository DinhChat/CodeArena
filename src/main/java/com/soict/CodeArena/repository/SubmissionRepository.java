package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.SUBMISSION_STATUS;
import com.soict.CodeArena.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByCreatedBy_UserIdOrderBySubmittedAtDesc(Long userId);

    List<Submission> findByProblem_ProblemIdOrderBySubmittedAtDesc(Long problemId);

    List<Submission> findByCreatedBy_UserIdAndProblem_ProblemIdOrderBySubmittedAtDesc(Long userId, Long problemId);

    List<Submission> findByStatus(SUBMISSION_STATUS status);
}
