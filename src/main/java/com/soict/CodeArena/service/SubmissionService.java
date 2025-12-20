package com.soict.CodeArena.service;

import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.DefaultSubmissionResponse;
import com.soict.CodeArena.response.SubmissionDetailResponse;
import com.soict.CodeArena.response.SubmissionItemResponse;

import java.util.List;

public interface SubmissionService {
    SubmissionDetailResponse submitSolution(SubmissionRequest request, String username) throws Exception;

    SubmissionDetailResponse getSubmissionById(Long submissionId, String username) throws Exception;

    List<DefaultSubmissionResponse> getMySubmissions(String username) throws Exception;

    List<SubmissionItemResponse> getSubmissionsByUserAndProblem(String username, Long problemId) throws Exception;
}
