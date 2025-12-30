package com.soict.CodeArena.repository;

import com.soict.CodeArena.model.Testcase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {
    Page<Testcase> findByProblem_ProblemId(Long problemId, Pageable pageable);

    Page<Testcase> findByProblem_ProblemIdAndIsSampleTrue(Long problemId, Pageable pageable);

    List<Testcase> findByProblem_ProblemIdOrderByOrderIndexAsc(Long problemId);
}
