export type Tag = "ARRAY" | "DP" | "STRING" | "HASHMAP" | "TWO_POINTERS" | "BINARY_SEARCH" | "TREE" | "GRAPH" | "DFS" | "BFS" | "GREEDY" | "TRIE" | "STACK" | "HASHSET" | "UNION_FIND" | "SORTING";
export type Difficulty = "EASY" | "MEDIUM" | "HARD";
export type Language = "PYTHON" | "CPP";

export interface TestCase {
    input: string;
    expectedOutput: string;
}

export interface Problem {
    id: string;
    title: string;
    description: string;
    difficulty: Difficulty;
    tags: Tag[];
    pythonStarterCode: string;
    cppStarterCode: string;
    pythonJudgeHarness: string;
    cppJudgeHarness: string;
    examples: string;
    constraints: string;
    testCases: TestCase[];
}

export interface EvaluationRequest {
    problemId: string;
    submittedCode: string;
    language: Language;
}

export interface EvaluationResult {
    passed: boolean;
    failedTestCaseIndices: number[];
    stdout: string;
    stderr: string;
}

export interface Xp {
    currentXp: number;
    newLevel: number;
    streak: number;
}