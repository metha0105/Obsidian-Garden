package com.obsidian.garden.dto.evaluation;

import com.obsidian.garden.model.enums.Language;

import java.util.UUID;

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
public class EvaluationRequestDto {
    private UUID problemId;
    private String submittedCode;
    private Language language;
}