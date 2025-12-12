package com.obsidian.garden.repository;

import com.obsidian.garden.model.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestCaseRepository extends JpaRepository<TestCase, UUID> {
    // Finds and lists all test cases for provided problem
    List<TestCase> findAllByProblem(Problem problem);
}