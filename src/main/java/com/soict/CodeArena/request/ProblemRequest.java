package com.soict.CodeArena.request;

import com.soict.CodeArena.model.DIFFICULTY_LEVEL;
import lombok.Data;

@Data
public class ProblemRequest {
    private String problemCode;
    private String title;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String sampleInput;
    private String sampleOutput;
    private String constraints;
    private DIFFICULTY_LEVEL difficultyLevel;
    private Integer timeLimit;
    private Integer memoryLimit;
}
