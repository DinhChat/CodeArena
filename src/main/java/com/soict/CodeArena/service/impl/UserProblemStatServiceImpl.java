package com.soict.CodeArena.service.impl;

import com.soict.CodeArena.model.*;
import com.soict.CodeArena.repository.UserProblemStatRepository;
import com.soict.CodeArena.service.UserProblemStatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProblemStatServiceImpl implements UserProblemStatService {
    private final UserProblemStatRepository statRepository;

    @Override
    @Transactional
    public void updateStatAfterJudging(Submission submission) {
        User user = submission.getCreatedBy();
        Problem problem = submission.getProblem();

        UserProblemStat stat = statRepository
                .findByUserAndProblem(user, problem)
                .orElse(null);

        if (stat == null) {
            stat = createNewStat(submission);
        } else {
            updateExistingStat(stat, submission);
        }

        stat.setLastSubmittedAt(submission.getJudgedAt());
        statRepository.save(stat);
    }

    private UserProblemStat createNewStat(Submission submission) {
        UserProblemStat stat = new UserProblemStat();
        stat.setUser(submission.getCreatedBy());
        stat.setProblem(submission.getProblem());
        stat.setSubmitCount(1);
        stat.setSubmission(submission);

        applyBestResult(stat, submission);
        return stat;
    }

    private void updateExistingStat(UserProblemStat stat, Submission submission) {
        stat.setSubmitCount(stat.getSubmitCount() + 1);

        if (isBetterSubmission(submission, stat)) {
            applyBestResult(stat, submission);
        }
    }

    private void applyBestResult(UserProblemStat stat, Submission submission) {
        stat.setBestStatus(submission.getStatus());
        stat.setBestPassedTestcases(submission.getPassedTestcases());
        stat.setTotalTestcases(submission.getTotalTestcases());
        stat.setSubmission(submission);

        stat.setBestScore(
                calculateScore(
                        submission.getPassedTestcases(),
                        submission.getTotalTestcases()
                )
        );
    }

    private boolean isBetterSubmission(Submission sub, UserProblemStat stat) {
        if (stat.getBestStatus() != SUBMISSION_STATUS.ACCEPTED
                && sub.getStatus() == SUBMISSION_STATUS.ACCEPTED) {
            return true;
        }
        if (sub.getStatus() == stat.getBestStatus()) {
            return sub.getPassedTestcases()
                    > stat.getBestPassedTestcases();
        }
        return false;
    }

    private int calculateScore(int passed, int total) {
        if (total == 0) return 0;
        return (int) ((passed * 100.0) / total);
    }
}
