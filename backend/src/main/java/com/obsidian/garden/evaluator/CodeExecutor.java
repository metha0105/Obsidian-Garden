package com.obsidian.garden.evaluator;

import com.obsidian.garden.model.enums.Language;
import com.obsidian.garden.dto.evaluation.EvaluationResultDto;
import com.obsidian.garden.model.TestCase;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

public class CodeExecutor {
    private static final String DOCKER_IMAGE_NAME = "obsidian-garden-cpp-evaluator";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EvaluationResultDto executeSubmittedCodeInDocker(String submittedCode, List<TestCase> testCases, Language language) {
        try {
            // 1: Create a temporary input.json file
            File inputJson = File.createTempFile("submission_input", ".json");
            writeInputPayload(submittedCode, testCases, language, inputJson);

            // 2: Launch Docker with the JSON file mounted
            ProcessBuilder pb = new ProcessBuilder(
                "docker", "run", "--rm",
                "-v", inputJson.getAbsolutePath() + ":/app/input.json",
                DOCKER_IMAGE_NAME
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 3: Capture evaluator's output
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
            }

            process.waitFor();

            // 4: Parse evaluator's JSON output
            return objectMapper.readValue(output.toString(), EvaluationResultDto.class);
        } catch (Exception e) {
            e.printStackTrace();

            return new EvaluationResultDto(false, List.of(), "", "Evaluation error: " + e.getMessage());
        }
    }

    // Converts Java objects into JSON input for the evaluator container
    private void writeInputPayload(String code, List<TestCase> testCases, Language language, File outputFile) throws IOException {
        var payload = new EvaluatorPayload(code, testCases, language);
        objectMapper.writeValue(outputFile, payload);
    }
}