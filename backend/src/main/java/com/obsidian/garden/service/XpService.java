package com.obsidian.garden.service;

import com.obsidian.garden.dto.xp.XpDto;
import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.model.SessionProgress;

import jakarta.servlet.http.HttpSession;

/**
 * Service interface for managing XP
 */
public interface XpService {

    /**
     * Loads the session progress from the HTTP session and if none exists, it initializes a new session progress
     * @param session Current HTTP session
     * @return The current SessionProgress object from the session or a new initialized one
     */
    SessionProgress loadSessionProgress(HttpSession session);

    /**
     * Awards XP to user based on problem difficuly, updates their streak, checks for milestones, and recalculates their level dynamically
     * @param difficulty Difficulty of the solved problem
     * @param session Current HTTP session
     * @param correct Whether the problem was solved correctly
     * @return An XpDto containing the XP awarded, current streak, new level, and flags for milestone achievement and levelling up
     */
    XpDto awardXp(Difficulty difficulty, HttpSession session, boolean correct);
}