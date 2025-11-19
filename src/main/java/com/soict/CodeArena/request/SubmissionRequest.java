package com.soict.CodeArena.request;

import lombok.Data;

@Data
public class SubmissionRequest {
    private Long problemId;
    private String code;
    private String language;
}
