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
    private String constraints;
    @Enumerated(EnumType.STRING)
    private DIFFICULTY_LEVEL difficultyLevel;
    private Integer timeLimit;
    private Integer memoryLimit;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean isActive = false;
}
