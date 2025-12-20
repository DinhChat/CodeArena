package com.soict.CodeArena.response;

import com.soict.CodeArena.model.SUBMISSION_STATUS;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DefaultSubmissionResponse {
    private Long submissionId;
    private String problemTitle;
    private String language;
    private SUBMISSION_STATUS status;
    private Integer passedTestcases;
    private Integer totalTestcases;
    private LocalDateTime judgedAt;
}
