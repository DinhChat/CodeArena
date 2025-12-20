package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.Problem;
import com.soict.CodeArena.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemCode(String problemCode);
    List<Problem> findByIsActive(boolean isActive);
    List<Problem> findAllByCreatedBy(User createdBy);
    @Query("""
    SELECT p, s
    FROM Problem p
    LEFT JOIN UserProblemStat s
        ON s.problem = p AND s.user = :user
    WHERE p.isActive = true
    """)
    List<Object[]> findActiveProblemsWithUserStat(
            @Param("user") User user);
}