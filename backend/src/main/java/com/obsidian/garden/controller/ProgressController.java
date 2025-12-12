package com.obsidian.garden.controller;

import com.obsidian.garden.dto.xp.XpDto;
import com.obsidian.garden.model.SessionProgress;
import com.obsidian.garden.service.XpService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final XpService xpService;

    public ProgressController(XpService xpService) {
        this.xpService = xpService;
    }

    /**
     * Retrieves the current session's progress including XP, level, and streak
     * @param session Current HTTP session
     * @return 200 (OK) with the current progress details in XpDto format
     */
    @GetMapping
    public XpDto getProgress(HttpSession session) {
        SessionProgress progress = xpService.loadSessionProgress(session);
        return new XpDto(
            progress.getXp(),
            progress.getLevel(),
            progress.getStreak()
        );
    }
}
