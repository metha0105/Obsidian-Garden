package com.obsidian.garden.service;

import com.obsidian.garden.dto.evaluation.EvaluationRequestDto;
import com.obsidian.garden.dto.evaluation.EvaluationResultDto;

/**
 * Service interface for evaluating user-submitted code against stored test cases
 */
public interface EvaluationService {
    /**
     * Evaluates user's code by compiling and executing it against the test cases associated with the specified problem ID
     * @param request DTO containing the problem ID, code submission, and the language
     * @return An EvaluationResultDto containing pass/fail status and output details (i.e., errors if applicable, etc.)
     */
    EvaluationResultDto evaluateCode(EvaluationRequestDto request);
}