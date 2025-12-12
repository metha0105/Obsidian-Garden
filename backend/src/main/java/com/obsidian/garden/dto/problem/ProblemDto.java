package com.obsidian.garden.dto.problem;

import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.model.enums.Tag;

import java.util.UUID;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ProblemDto {
    private UUID id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private List<Tag> tags;
    private String pythonStarterCode;
    private String cppStarterCode;
    private String pythonJudgeHarness;
    private String cppJudgeHarness;
    private String examples;
    private String constraints;
    private List<TestCaseDto> testCases;
}