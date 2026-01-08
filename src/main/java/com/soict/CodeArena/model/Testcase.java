package com.soict.CodeArena.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testcaseId;
    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String input;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String expectedOutput;
    private boolean isSample = false;
    private Integer orderIndex;
}
