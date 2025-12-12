package com.obsidian.garden.model;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "test_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"problem"})
@ToString(exclude = {"problem"})
public class TestCase {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String input;

    @Column(nullable = false)
    private String expectedOutput;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    public TestCase(String input, String expectedOutput, Problem problem)
    {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.problem = problem;
    }
}