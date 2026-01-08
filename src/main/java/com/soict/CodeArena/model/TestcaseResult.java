package com.soict.CodeArena.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestcaseResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testcaseResultId;
    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;
    private Integer testcaseNumber;
    @Column(columnDefinition = "LONGTEXT")
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private Double timeTaken;
    private Integer memoryUsed;
    private String status;
    private Boolean passed;
}
