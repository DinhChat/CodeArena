package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.User;
import com.soict.CodeArena.model.UserProblemStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProblemStatRepository extends JpaRepository<UserProblemStat,Long> {
    Optional<UserProblemStat> findByUserAndProblem(User user, Problem problem);
    void deleteByProblem_ProblemId(Long problemId);
    void deleteByUser_UserId(Long userId);
    void deleteBySubmission_SubmissionId(Long submissionId);
    @Query("""
        select coalesce(sum(ups.bestScore), 0)
        from UserProblemStat ups
        join ups.problem p
        where ups.user.userId = :userId
          and p.createdBy.userId = :adminId
    """)
    Integer getTotalScoreByUserAndAdmin(
            @Param("userId") Long userId,
            @Param("adminId") Long adminId
    );
}
