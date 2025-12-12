package com.obsidian.garden.service.impl;

import com.obsidian.garden.dto.xp.XpDto;
import com.obsidian.garden.model.*;
import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.service.XpService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class XpServiceImpl implements XpService {
    
    public XpServiceImpl() {}

    @Override
    public SessionProgress loadSessionProgress(HttpSession session) {
        SessionProgress progress = (SessionProgress) session.getAttribute("sessionProgress");

        if (progress == null) {
            progress = new SessionProgress().initial();
            session.setAttribute("sessionProgress", progress);
            return progress;
        } else {
            return progress;
        }
    }

    @Override
    public XpDto awardXp(Difficulty difficulty, HttpSession session, boolean correct) {
        SessionProgress progress = loadSessionProgress(session);
        
        int xp = switch (difficulty) {
            case EASY -> 30;
            case MEDIUM -> 60;
            case HARD -> 90;
        };

        int xpNeeded = 5 * progress.getLevel() * progress.getLevel() + 10 * progress.getLevel();

        if (correct) {
            // Awarding XP
            progress.setXp(progress.getXp() + xp);

            // Updating streak (based on # of correct submissions)
            progress.setStreak(progress.getStreak() + 1);

            // Checking for level up
            if (progress.getXp() >= xpNeeded) {
                progress.setLevel(progress.getLevel() + 1);
                progress.setXp(progress.getXp() - xpNeeded);
            }
        }

        return new XpDto(
            progress.getXp(),
            progress.getLevel(),
            progress.getStreak()
        );
    }
}