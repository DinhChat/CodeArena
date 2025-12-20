package com.soict.CodeArena.service;

import com.soict.CodeArena.model.Submission;

public interface UserProblemStatService {
    void updateStatAfterJudging(Submission submission);
}
