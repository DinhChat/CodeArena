package com.soict.CodeArena.response;

import lombok.Data;

@Data
public class TestcaseResponse {
    private Long testcaseId;
    private String input;
    private String expectedOutput;
    private boolean isSample;
    private Integer orderIndex;
}
