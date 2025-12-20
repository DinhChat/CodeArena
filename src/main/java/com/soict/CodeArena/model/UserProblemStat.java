package com.soict.CodeArena.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_problem_stat",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "problem_id"})
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProblemStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;
    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;
    @Enumerated(EnumType.STRING)
    private SUBMISSION_STATUS bestStatus;
    private Integer bestPassedTestcases;
    private Integer totalTestcases;
    private Integer bestScore;
    private Integer submitCount;
    private LocalDateTime lastSubmittedAt;
}
