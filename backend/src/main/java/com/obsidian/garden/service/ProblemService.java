package com.obsidian.garden.service;

import java.util.UUID;
import java.util.List;

import com.obsidian.garden.dto.problem.*;
import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.model.enums.Tag;

/**
 * Service interface for retrieving and managing LeetCode problems
 */
public interface ProblemService {
    
    /**
     * Retrieves problems filtered by provided tag(s) and/or difficulties
     * @param difficulties Desired difficulty levels
     * @param tag Desired category to filter by (e.g., array, DP, etc.)
     * @return List of matching problems as ProblemDto objects
     */
    List<ProblemDto> retrieveFilteredProblems(List<Difficulty> difficulties, List<Tag> tags);

    /**
     * Retrieves problem by UUID
     * @param id UUID of the problem
     * @return Corresponding ProblemDto and throws a 'NoSuchelementException' if no problem is found
     */
    ProblemDto retrieveProblemById(UUID id);
}