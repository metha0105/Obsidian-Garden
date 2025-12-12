package com.obsidian.garden.service.impl;

import com.obsidian.garden.dto.problem.*;
import com.obsidian.garden.model.*;
import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.model.enums.Tag;
import com.obsidian.garden.repository.ProblemRepository;
import com.obsidian.garden.service.ProblemService;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ProblemServiceImpl implements ProblemService {
    
    private final ProblemRepository problemRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Override
    public List<ProblemDto> retrieveFilteredProblems(List<Difficulty> difficulties, List<Tag> tags) {
        Set<Tag> tagSet = (tags == null) ? Set.of() : new HashSet<>(tags);
        long tagsNum = tagSet.size();

        var problems = problemRepository.findByDifficultiesAndTags(difficulties, tagSet, tagsNum);
        return problems.stream().map(this::convertToProblemDto).toList();
    }

    @Override
    public ProblemDto retrieveProblemById(UUID id) {
        Problem problem = problemRepository.findById(id).orElse(null);

        if (problem == null) {
            return null;
        }

        return convertToProblemDto(problem);
    }
    
    private TestCaseDto convertToTestCaseDto(TestCase tc) {
        return new TestCaseDto(
            tc.getInput(),
            tc.getExpectedOutput()
        );
    }

    private ProblemDto convertToProblemDto(Problem p) {
        List<TestCaseDto> testCaseDtos = p.getTestCases().stream().map(this::convertToTestCaseDto).collect(Collectors.toList());

        var tags = (p.getTags() == null) ? List.<Tag>of() 
            : p.getTags().stream()
                .sorted(Comparator.comparing(Enum::name))
                .toList();
        
        return new ProblemDto(
            p.getId(),
            p.getTitle(),
            p.getDescription(),
            p.getDifficulty(),
            tags,
            p.getPythonStarterCode(),
            p.getCppStarterCode(),
            p.getPythonJudgeHarness(),
            p.getCppJudgeHarness(),
            p.getExamples(),
            p.getConstraints(),
            testCaseDtos
        );
    }
}