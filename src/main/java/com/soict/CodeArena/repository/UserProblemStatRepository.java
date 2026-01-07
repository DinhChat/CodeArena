package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProblemStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProblemStatRepository extends JpaRepository<UserProblemStat,Long> {
    Optional<UserProblemStat> findByUserAndProblem(User user, Problem problem);
    void deleteByProblem_ProblemId(Long problemId);
    void deleteByUser_UserId(Long userId);
    void deleteBySubmission_SubmissionId(Long submissionId);
}
