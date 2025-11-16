package com.soict.CodeArena.request;

import lombok.Data;

@Data
public class TestcaseRequest {
    private Long problemId;
    private String input;
    private String expectedOutput;
    private boolean isSample;
    private Integer orderIndex;
}
