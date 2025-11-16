package com.soict.CodeArena.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubmissionPayload {
    @JsonProperty("submission_code")
    private String submissionCode;
    @JsonProperty("language")
    private String language;
    @JsonProperty("test_cases")
    private List<TestCaseDTO> testcases;
    @JsonProperty("time_limit")
    private Integer timeLimit;
    @JsonProperty("memory_limit")
    private Integer memoryLimit;
}
