package com.soict.CodeArena.response;

import com.soict.CodeArena.model.SUBMISSION_STATUS;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionResponse {
    private Long submissionId;
    private String problemCode;
    private String problemTitle;
    private String username;
    private String language;
    private SUBMISSION_STATUS status;
    private Integer executionTime;
    private Integer memoryUsed;
    private String errorMessage;
    private Integer passedTestcases;
    private Integer totalTestcases;
    private LocalDateTime submittedAt;
    private LocalDateTime judgedAt;
}
