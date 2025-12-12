package com.obsidian.garden.repository;

import com.obsidian.garden.model.*;
import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.model.enums.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
import java.util.Set;

public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    // Filters by difficulties and tags, both optional
    @Query("""
    SELECT p
    FROM Problem p
    WHERE (:difficulties IS NULL OR p.difficulty IN :difficulties)
        AND (
            :tagsSize = 0 OR EXISTS (
                SELECT 1
                FROM Problem p2 JOIN p2.tags t2
                WHERE p2 = p AND t2 IN :tags
            )
        )
    ORDER BY
        CASE
            WHEN p.difficulty = com.obsidian.garden.model.enums.Difficulty.EASY   THEN 1
            WHEN p.difficulty = com.obsidian.garden.model.enums.Difficulty.MEDIUM THEN 2
            WHEN p.difficulty = com.obsidian.garden.model.enums.Difficulty.HARD   THEN 3
            ELSE 4
        END,
        p.title
    """)
    List<Problem> findByDifficultiesAndTags(List<Difficulty> difficulties, Set<Tag> tags, long tagsSize);

    // No filters, just returns all problems ordered by difficulty
    @Query("""
        SELECT p FROM Problem p
        ORDER BY CASE
        WHEN p.difficulty = com.obsidian.garden.model.enums.Difficulty.EASY   THEN 1
        WHEN p.difficulty = com.obsidian.garden.model.enums.Difficulty.MEDIUM THEN 2
        WHEN p.difficulty = com.obsidian.garden.model.enums.Difficulty.HARD   THEN 3
        ELSE 4
        END
    """)
    List<Problem> findAllOrderByDifficultyLevel();
}