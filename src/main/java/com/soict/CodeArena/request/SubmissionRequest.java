package com.soict.CodeArena.request;

import lombok.Data;

@Data
public class SubmissionRequest {
    private String problemId;
    private String code;
    private String language;
}
