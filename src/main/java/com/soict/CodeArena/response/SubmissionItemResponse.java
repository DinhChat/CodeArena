package com.soict.CodeArena.response;

import com.soict.CodeArena.model.SUBMISSION_STATUS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionItemResponse {
    private Long submissionId;
    private SUBMISSION_STATUS status;
    private Integer passedTestcases;
    private Integer totalTestcases;
    private Integer executionTime;
    private Integer memoryUsed;
    private boolean selected;
}
