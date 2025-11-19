package com.soict.CodeArena.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SandboxResponse {

    private Meta meta;
    private List<TestcaseResult> results;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        @JsonProperty("total_test_cases")
        private Integer totalTestCases;

        @JsonProperty("passed_test_cases")
        private Integer passedTestCases;

        @JsonProperty("all_passed")
        private Boolean allPassed;

        @JsonProperty("compilation_error")
        private String compilationError;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestcaseResult {
        @JsonProperty("test_case_number")
        private Integer testCaseNumber;

        private String input;

        @JsonProperty("expected_output")
        private String expectedOutput;

        @JsonProperty("actual_output")
        private String actualOutput;

        @JsonProperty("time_taken")
        private Double timeTaken;

        @JsonProperty("memory_used")
        private Integer memoryUsed;

        private String status;
        private Boolean passed;
    }
}

