package com.soict.CodeArena.response;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import lombok.Data;

@Data
public class DefaultProblemResponse {
    private Long problemId;
    private String title;
    private String problemCode;
    private DIFFICULTY_LEVEL difficultyLevel;
    private boolean isActive;
}
