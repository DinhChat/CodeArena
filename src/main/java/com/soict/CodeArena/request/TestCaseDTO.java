package com.soict.CodeArena.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class TestCaseDTO {
    @JsonProperty("input")
    @JsonSerialize(using = ToStringSerializer.class)
    private String input;
    @JsonProperty("expected_output")
    @JsonSerialize(using = ToStringSerializer.class)
    private String expectedOutput;

    public TestCaseDTO(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }
}
