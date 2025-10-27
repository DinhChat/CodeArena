package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.SUBMISSION_STATUS;
import com.soict.CodeArena.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByUser_UidOrderBySubmittedAtDesc(Long userId);

    List<Submission> findByProblem_ProblemIdOrderBySubmittedAtDesc(Long problemId);

    List<Submission> findByUser_UidAndProblem_ProblemIdOrderBySubmittedAtDesc(Long userId, Long problemId);

    List<Submission> findByStatus(SUBMISSION_STATUS status);
}
