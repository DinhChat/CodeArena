package com.soict.CodeArena.response;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import com.soict.CodeArena.model.SUBMISSION_STATUS;
import lombok.Data;

@Data
public class ProblemItemResponse {
    private Long problemId;
    private String problemCode;
    private String title;
    private DIFFICULTY_LEVEL difficultyLevel;

    private boolean attempted;
    private SUBMISSION_STATUS bestStatus;
    private Integer bestScore;
}
