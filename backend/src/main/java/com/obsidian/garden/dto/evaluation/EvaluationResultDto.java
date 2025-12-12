package com.obsidian.garden.dto.evaluation;

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
public class EvaluationResultDto {
    private boolean passed;
    private List<Integer> failedTestCaseIndices;
    private String stdout;
    private String stderr;
}