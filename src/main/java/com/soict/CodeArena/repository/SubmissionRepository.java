package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.SUBMISSION_STATUS;
import com.soict.CodeArena.model.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Page<Submission> findByCreatedBy_UserId(Long userId, Pageable pageable);

    Page<Submission> findByProblem_ProblemId(Long problemId, Pageable pageable);

    Page<Submission> findByCreatedBy_UserIdAndProblem_ProblemId(Long userId, Long problemId, Pageable pageable);

    List<Submission> findByStatus(SUBMISSION_STATUS status);

    Page<Submission> findByStatus(SUBMISSION_STATUS status, Pageable pageable);

    List<Submission> findByProblem_ProblemIdAndCreatedBy_UserIdOrderBySubmittedAtDesc(Long problemId, Long userId);
}
