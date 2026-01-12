package com.soict.CodeArena.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Double executionTime;
    private Integer memoryUsed;
    private Integer passedTestcases;
    private Integer totalTestcases;
    private LocalDateTime submittedAt;
    private LocalDateTime judgedAt;

    @OneToMany(
            mappedBy = "submission",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @ToString.Exclude
    private List<TestcaseResult> testcaseResults = new ArrayList<>();
}
