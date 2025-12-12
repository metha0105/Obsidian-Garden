package com.obsidian.garden.controller;

import com.obsidian.garden.dto.problem.ProblemDto;
import com.obsidian.garden.dto.evaluation.EvaluationRequestDto;
import com.obsidian.garden.dto.evaluation.EvaluationResultDto;
import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.model.enums.Tag;
import com.obsidian.garden.service.ProblemService;
import com.obsidian.garden.service.XpService;
import com.obsidian.garden.service.EvaluationService;

import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {
    
    private final ProblemService problemService;
    private final XpService xpService;
    private final EvaluationService evaluationService;

    public ProblemController(ProblemService problemService, XpService xpService, EvaluationService evaluationService) {
        this.problemService = problemService;
        this.xpService = xpService;
        this.evaluationService = evaluationService;
    }

    /**
     * 
     * @param difficulty Optional filters (i.e., easy, medium, or hard)
     * @param tags Optional tags/categories (e.g., 'dp', 'arrays', etc.)
     * @return 200 (OK) with list of matching problems ordered by difficulty
     */
    @GetMapping
    public ResponseEntity<List<ProblemDto>> getProblems(@RequestParam(required = false) List<Difficulty> difficulties, @RequestParam(required = false) List<Tag> tags) {
        List<ProblemDto> problems = problemService.retrieveFilteredProblems(difficulties, tags);

        return ResponseEntity.ok(problems);
    }

    /**
     * 
     * @param id UUID of the problem
     * @return 200 (OK) with all the problem details (including starter code and test cases)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProblemDto> getProblemById(@PathVariable UUID id) {
        ProblemDto problem = problemService.retrieveProblemById(id);

        if (problem == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(problem);
    }

    /**
     * 
     * @param id UUID of the problem
     * @param request EvaluationRequestDto containing the user's code submission
     * @param session HttpSession to track user progress and award XP
     * @return 200 (OK) with the evaluation results
     */
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<EvaluationResultDto> postProblemEvaluation(@PathVariable UUID id, @RequestBody EvaluationRequestDto request, HttpSession session) {
        EvaluationResultDto result = evaluationService.evaluateCode(request);

        boolean correct = result.isPassed();
        Difficulty difficulty = problemService.retrieveProblemById(id).getDifficulty();
        xpService.awardXp(difficulty, session, correct);

        return ResponseEntity.ok(result);
    }
}