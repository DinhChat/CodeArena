package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import com.soict.CodeArena.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemCode(String problemCode);

    List<Problem> findByDifficultyLevel(DIFFICULTY_LEVEL difficultyLevel);

    List<Problem> findByIsActive(boolean isActive);

    List<Problem> findByCreatedBy_Uid(Long userId);
}
