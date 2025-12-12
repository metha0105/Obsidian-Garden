package com.obsidian.garden.service.impl;

import com.obsidian.garden.dto.evaluation.EvaluationRequestDto;
import com.obsidian.garden.dto.evaluation.EvaluationResultDto;
import com.obsidian.garden.model.*;
import com.obsidian.garden.repository.ProblemRepository;
import com.obsidian.garden.repository.TestCaseRepository;
import com.obsidian.garden.service.EvaluationService;
import com.obsidian.garden.service.XpService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EvaluationServiceImpl implements EvaluationService {
    
    private final TestCaseRepository testCaseRepository;
    private final ProblemRepository problemRepository;
    private static final Logger logger = LoggerFactory.getLogger(EvaluationServiceImpl.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EvaluationServiceImpl(TestCaseRepository testCaseRepository, ProblemRepository problemRepository, XpService xpService) {
        this.testCaseRepository = testCaseRepository;
        this.problemRepository = problemRepository;
    }

    @Override
    public EvaluationResultDto evaluateCode(EvaluationRequestDto request) {
        Path inputPath = null;
        Path tempDir = null;

        try {
            Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException("Problem not found"));

            String judgeHarness;
            switch (request.getLanguage()) {
                case CPP -> judgeHarness = problem.getCppJudgeHarness();
                case PYTHON -> judgeHarness = problem.getPythonJudgeHarness();
                default -> throw new IllegalArgumentException("Unsupported language: " + request.getLanguage());
            }
            String judgeHarnessJson = objectMapper.writeValueAsString(judgeHarness);
            String userCodeJson = objectMapper.writeValueAsString(request.getSubmittedCode());
            
            List<TestCase> testCases = testCaseRepository.findAllByProblem(problem);

            // Builds a JSON array of test cases
            StringBuilder testCasesJsonArray = new StringBuilder("[");
            for (int i = 0; i < testCases.size(); i++) {
                TestCase tc = testCases.get(i);

                testCasesJsonArray.append(String.format(
                    "{\"input\":\"%s\", \"expected\":\"%s\"}",
                    escape(tc.getInput()), escape(tc.getExpectedOutput())
                ));

                if (i != testCases.size() - 1) {
                    testCasesJsonArray.append(", ");
                }
            }
            testCasesJsonArray.append("]");

            // Creates temporary directory and input JSON file
            tempDir = Files.createTempDirectory("eval-" + UUID.randomUUID());
            inputPath = tempDir.resolve("input.json");

            String inputJson = """
                {
                    "userCode": %s,
                    "judgeHarness": %s,
                    "language": "%s",
                    "testCases": %s
                }
                """.formatted(
                    userCodeJson,
                    judgeHarnessJson,
                    request.getLanguage().name(),
                    testCasesJsonArray
                );
            
            // Debugging
            System.out.println("DEBUG inputJson: " + inputJson);
            System.out.println("DEBUG inputJson length: " + inputJson.length());
            Files.writeString(inputPath, inputJson);

            // Run docker container
            ProcessBuilder builder = new ProcessBuilder(
                "docker", "run", "--rm",
                "-v", inputPath.toAbsolutePath() + ":/app/input.json",
                "obsidian-evaluator"
            );
            builder.redirectErrorStream(true);

            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // DEBUGGING
            reader.lines().forEach(System.out::println);

            // DEBUGGING
            BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            err.lines().forEach(System.out::println);

            StringBuilder stdout = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                stdout.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return new EvaluationResultDto(false, List.of(), "", "Evaluator process exited with code " + exitCode);
            }

            // Parse JSON response IF successful
            EvaluationResultDto result = objectMapper.readValue(stdout.toString(), EvaluationResultDto.class);
            return result;
        }
        catch (Exception e) {
            return new EvaluationResultDto(false, List.of(), "", "Evaluation failed: " + e.getMessage());
        }
        finally {
            try {
                if (inputPath != null) {
                    Files.deleteIfExists(inputPath);
                }

                if (tempDir != null) {
                    Files.deleteIfExists(tempDir);
                }
            }
            catch (IOException cleanupE) {
                logger.warn("Failed to clean up temp files after evaluation", cleanupE);
            }
        }
    }

    private String escape(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r");
    }
}