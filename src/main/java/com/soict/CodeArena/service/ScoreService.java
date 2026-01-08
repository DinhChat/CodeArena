package com.soict.CodeArena.service;

import com.soict.CodeArena.model.User;
import com.soict.CodeArena.repository.UserProblemStatRepository;
import com.soict.CodeArena.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {
    private final UserProblemStatRepository statRepository;
    private final UserRepository userRepository;

    public ScoreService(UserProblemStatRepository statRepository, UserRepository userRepository) {
        this.statRepository = statRepository;
        this.userRepository = userRepository;
    }

    public Integer getUserScoreInClass(String username, Long adminId) {
        User user = userRepository.findByUsername(username);
        return statRepository.getTotalScoreByUserAndAdmin(user.getUserId(), adminId);
    }
}