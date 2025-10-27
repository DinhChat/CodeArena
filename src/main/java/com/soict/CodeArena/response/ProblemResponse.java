package com.soict.CodeArena.response;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProblemResponse {
    private Long problemId;
    private String problemCode;
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String constraints;
    private DIFFICULTY_LEVEL difficultyLevel;
    private Integer timeLimit;
    private Integer memoryLimit;
    private String createdBy;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean isActive;
}
