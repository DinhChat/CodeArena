package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.Testcase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {
    List<Testcase> findByProblem_ProblemIdOrderByOrderIndexAsc(Long problemId);

    List<Testcase> findByProblem_ProblemIdAndIsSampleTrue(Long problemId);
}
