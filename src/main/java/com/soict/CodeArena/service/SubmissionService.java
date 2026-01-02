package com.soict.CodeArena.service;

import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.DefaultSubmissionResponse;
import com.soict.CodeArena.response.PagedResponse;
import com.soict.CodeArena.response.SubmissionDetailResponse;
import com.soict.CodeArena.response.SubmissionItemResponse;
import org.springframework.web.server.ResponseStatusException;

public interface SubmissionService {
    SubmissionDetailResponse submitSolution(SubmissionRequest request, String username) throws Exception;

    SubmissionDetailResponse getSubmissionById(Long submissionId, String username) throws Exception;

    PagedResponse<DefaultSubmissionResponse> getMySubmissions(String username, Integer page, Integer pageSize,
            Integer offset) throws ResponseStatusException;

    PagedResponse<SubmissionItemResponse> getSubmissionsByUserAndProblem(String username, Long problemId, Integer page,
            Integer pageSize, Integer offset) throws ResponseStatusException;
}
