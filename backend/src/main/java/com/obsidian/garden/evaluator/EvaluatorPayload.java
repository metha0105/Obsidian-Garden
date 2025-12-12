package com.obsidian.garden.evaluator;

import com.obsidian.garden.model.TestCase;
import com.obsidian.garden.model.enums.Language;

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
public class EvaluatorPayload {
    public String code;
    public List<TestCaseWrapper> testCases;
    public String language;

    public EvaluatorPayload(String code, List<TestCase> testCases, Language languageEnum) {
        this.code = code;
        this.language = languageEnum.name();
        this.testCases = testCases.stream()
            .map(tc -> new TestCaseWrapper(tc.getInput(), tc.getExpectedOutput()))
            .toList();
    }
}
