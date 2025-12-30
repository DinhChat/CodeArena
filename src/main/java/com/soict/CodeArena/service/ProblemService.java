package com.soict.CodeArena.service;

import com.soict.CodeArena.request.ProblemRequest;
import com.soict.CodeArena.response.DefaultProblemResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.ProblemItemResponse;
import com.soict.CodeArena.response.ProblemDetailResponse;

import java.util.List;

public interface ProblemService {
    ProblemDetailResponse createProblem(ProblemRequest request, String username) throws Exception;

    ProblemDetailResponse updateProblem(Long problemId, ProblemRequest request, String username) throws Exception;

    ProblemDetailResponse activeProblem(Long problemId, String username) throws Exception;

    ProblemDetailResponse getProblemById(Long problemId) throws Exception;

    ProblemDetailResponse getProblemByCode(String problemCode) throws Exception;

    PagedResponse<DefaultProblemResponse> getMyProblems(String username, Integer page, Integer pageSize, Integer offset)
            throws Exception;

    PagedResponse<ProblemItemResponse> getActiveProblems(String username, Integer page, Integer pageSize,
            Integer offset) throws Exception;

    void deleteProblem(Long problemId, String username) throws Exception;
}
