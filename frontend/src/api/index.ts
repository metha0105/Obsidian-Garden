import axios from "axios";
import type {
    Tag,
    Difficulty,
    Problem,
    EvaluationRequest,
    EvaluationResult,
    Xp,
} from "../types/index";
import { API_BASE_URL } from "../config";

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    withCredentials: true,
});

// Helper to return data
function unwrap<T>(promise: Promise<{ data: T }>): Promise<T> {
    return promise.then((response) => response.data);
}

export const Api = {
    // Endpoint #1: List all problems with optional filters
    getProblems: (opts?: {
        difficulties?: Difficulty[];
        tags?: Tag[];
    }): Promise<Problem[]> => {
        return unwrap(
            apiClient.get<Problem[]>("/api/problems", {
                params: {
                    difficulties: opts?.difficulties,
                    tags: opts?.tags,
                }
            })
        );
    },

    // Endpoint #2: Get problem details by ID
    getProblemById: (id: string): Promise<Problem> => {
        return unwrap(apiClient.get<Problem>(`/api/problems/${id}`));
    },

    // Endpoint #3: Evaluate submitted code
    evaluateProblem: (id: string, body: EvaluationRequest): Promise<EvaluationResult> => {
        return unwrap(apiClient.post<EvaluationResult>(`/api/problems/${id}/evaluate`, body));
    },

    // Endpoint #4: Get user XP, lvl, and streak
    getStats: (): Promise<Xp> => {
        return unwrap(apiClient.get<Xp>("/api/progress"));
    }
};