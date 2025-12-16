package com.soict.CodeArena.service;

import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.SubmissionResponse;

import java.util.List;

public interface SubmissionService {
    SubmissionResponse submitSolution(SubmissionRequest request, String username) throws Exception;

    SubmissionResponse getSubmissionById(Long submissionId, String username) throws Exception;

    List<SubmissionResponse> getMySubmissions(String username) throws Exception;

    List<SubmissionResponse> getSubmissionsByUserAndProblem(String username, Long problemId) throws Exception;
}
