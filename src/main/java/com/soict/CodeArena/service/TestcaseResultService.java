package com.soict.CodeArena.service;

import com.soict.CodeArena.response.UITestcaseResultResponse;

import java.util.List;

public interface TestcaseResultService {
    List<UITestcaseResultResponse> getAllTestcaseResult(Long submissionId, String username) throws Exception;
}
