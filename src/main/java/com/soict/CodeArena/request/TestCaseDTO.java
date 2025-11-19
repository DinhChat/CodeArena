package com.soict.CodeArena.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestCaseDTO {
    @JsonProperty("input")
    private String input;
    @JsonProperty("expected_output")
    private String expectedOutput;

    public TestCaseDTO(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }
}
