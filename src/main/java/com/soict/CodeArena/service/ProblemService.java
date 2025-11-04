package com.soict.CodeArena.service;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import com.soict.CodeArena.request.ProblemRequest;
import com.soict.CodeArena.response.ProblemResponse;

import java.util.List;

public interface ProblemService {
    ProblemResponse createProblem(ProblemRequest request, String username) throws Exception;

    ProblemResponse updateProblem(Long problemId, ProblemRequest request, String username) throws Exception;

    ProblemResponse getProblemById(Long problemId) throws Exception;

    ProblemResponse getProblemByCode(String problemCode) throws Exception;

    List<ProblemResponse> getAllProblems();

    List<ProblemResponse> getProblemsByDifficulty(DIFFICULTY_LEVEL difficulty);

    List<ProblemResponse> getActiveProblems();

    void deleteProblem(Long problemId, String username) throws Exception;
}
