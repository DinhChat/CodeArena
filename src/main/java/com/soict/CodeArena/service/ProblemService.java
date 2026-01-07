package com.soict.CodeArena.service;

import com.soict.CodeArena.request.ProblemRequest;
import com.soict.CodeArena.response.DefaultProblemResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.ProblemItemResponse;
import com.soict.CodeArena.response.ProblemDetailResponse;
import org.springframework.web.server.ResponseStatusException;

public interface ProblemService {
    ProblemDetailResponse createProblem(ProblemRequest request, String username) throws ResponseStatusException;

    ProblemDetailResponse updateProblem(Long problemId, ProblemRequest request, String username) throws ResponseStatusException;

    ProblemDetailResponse activeProblem(Long problemId, String username) throws ResponseStatusException;

    ProblemDetailResponse getProblemById(Long problemId) throws ResponseStatusException;

    ProblemDetailResponse getProblemByCode(String problemCode) throws ResponseStatusException;

    PagedResponse<DefaultProblemResponse> getMyProblems(String username, Integer page, Integer pageSize, Integer offset)
            throws ResponseStatusException;

    PagedResponse<ProblemItemResponse> getActiveProblems(String username, Integer page, Integer pageSize,
            Integer offset) throws ResponseStatusException;

    void deleteProblem(Long problemId, String username) throws ResponseStatusException;
    PagedResponse<ProblemItemResponse> getAllProblemsOfAdmin(Long adminId, String username, Integer page, Integer pageSize, Integer offset);
}
