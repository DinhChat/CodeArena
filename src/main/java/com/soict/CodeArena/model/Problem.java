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
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;
    @Column(nullable = false, unique = true)
    private String problemCode;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String inputFormat;
    @Column(columnDefinition = "TEXT")
    private String outputFormat;
    @Column(columnDefinition = "TEXT")
    private String sampleInput;
    @Column(columnDefinition = "TEXT")
    private String sampleOutput;
    @Column(columnDefinition = "TEXT")
    private String constraints;
    @Enumerated(EnumType.STRING)
    private DIFFICULTY_LEVEL difficultyLevel;
    private Integer timeLimit;
    private Integer memoryLimit;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean isActive = false;
    @OneToMany(
            mappedBy = "problem",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    @JsonIgnore
    @ToString.Exclude
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(
            mappedBy = "problem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @ToString.Exclude
    private List<Testcase> testcases = new ArrayList<>();
}
