package com.soict.CodeArena.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UITestcaseResultResponse {
    private Integer testcaseNumber;
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private Double timeTaken;
    private Integer memoryUsed;
    private String status;
    private Boolean passed;
}
