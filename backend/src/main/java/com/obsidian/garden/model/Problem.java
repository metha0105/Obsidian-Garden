package com.obsidian.garden.model;
import com.obsidian.garden.model.enums.Difficulty;
import com.obsidian.garden.model.enums.Tag;

import java.util.UUID;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"testCases"})
@ToString(exclude = {"testCases"})
public class Problem {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String constraints;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String examples;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @ElementCollection(targetClass = Tag.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "problem_tags", joinColumns = @JoinColumn(name = "problem_id"))
    @Column(name = "tags", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Tag> tags = new HashSet<>();

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String pythonStarterCode;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String cppStarterCode;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String pythonJudgeHarness;

    @Column(unique = true, nullable = false, columnDefinition = "TEXT")
    private String cppJudgeHarness;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL)
    private List<TestCase> testCases;

    public Problem(String title, String description, Difficulty difficulty, Set<Tag> tags, String pythonStarterCode, String cppStarterCode, String pythonJudgeHarness, String cppJudgeHarness)
    {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.pythonStarterCode = pythonStarterCode;
        this.cppStarterCode = cppStarterCode;
        this.pythonJudgeHarness = pythonJudgeHarness;
        this.cppJudgeHarness = cppJudgeHarness;
    }
}