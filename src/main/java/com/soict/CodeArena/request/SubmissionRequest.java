package com.soict.CodeArena.request;

import lombok.Data;

@Data
public class SubmissionRequest {
    private String problemCode;
    private String code;
    private String language;
}
