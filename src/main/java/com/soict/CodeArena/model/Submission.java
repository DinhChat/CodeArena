package com.soict.CodeArena.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;
    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;
    @Column(nullable = false)
    private String language;
    @Enumerated(EnumType.STRING)
    private SUBMISSION_STATUS status = SUBMISSION_STATUS.PENDING;
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    private Integer executionTime;
    private Integer memoryUsed;
    private Integer passedTestcases;
    private Integer totalTestcases;
    private LocalDateTime submittedAt;
    private LocalDateTime judgedAt;
}
