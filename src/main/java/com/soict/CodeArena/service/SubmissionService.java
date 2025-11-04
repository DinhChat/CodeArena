package com.soict.CodeArena.service;

import com.soict.CodeArena.request.SubmissionRequest;
import com.soict.CodeArena.response.SubmissionResponse;

import java.util.List;

public interface SubmissionService {
    SubmissionResponse submitSolution(SubmissionRequest request, String username) throws Exception;

    SubmissionResponse getSubmissionById(Long submissionId) throws Exception;

    List<SubmissionResponse> getSubmissionsByUser(String username) throws Exception;

    List<SubmissionResponse> getSubmissionsByProblem(Long problemId);

    List<SubmissionResponse> getSubmissionsByUserAndProblem(String username, Long problemId) throws Exception;
}
