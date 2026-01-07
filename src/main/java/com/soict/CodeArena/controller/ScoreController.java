package com.soict.CodeArena.controller;

import com.soict.CodeArena.service.ScoreService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/score")
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/{adminId}/score")
    public Integer getMyScoreInClass(
            @PathVariable Long adminId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return scoreService.getUserScoreInClass(username, adminId);
    }
}
