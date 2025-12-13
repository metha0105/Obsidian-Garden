import { useEffect, useState } from "react";
import { Api } from "../../api";
import { useNavigate, useParams } from "react-router-dom";
import { type Language, type Problem, type EvaluationRequest, type EvaluationResult, type Xp } from "../../types/index";
import "./ProblemPage.css";
import Split from "react-split";
import EditorSection from "../../components/EditorSection/EditorSection";
import tempBg from "../../assets/temp-lune-bg.jpeg";
import tempSprite from "../../assets/mew.gif";
import { motion } from "framer-motion";
import LoadingPage from "../../components/LoadingPage/LoadingPage";

const variants = {
    initial: (direction: number) => ({ x: direction > 0 ? "100%" : "-100%", opacity: 0 }),
    animate: { x: 0, opacity: 1 },
    exit: (direction: number) => ({ x: direction > 0 ? "-100%" : "100%", opacity: 0 }),
};

type ProblemPageProps = {
    direction: number;
    setDirection: (dir: number) => void;
}

const ProblemPage: React.FC<ProblemPageProps> = ({ direction, setDirection }) => {
    const navigate = useNavigate();

    const { problemId } = useParams();

    const [problem, setProblem] = useState<Problem | null>(null);
    const [xp, setXp] = useState<Xp | null>(null);

    const [waking, setWaking] = useState(true);
    const [loadingData, setLoadingData] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [backendReady, setBackendReady] = useState(false);

    const [language, setLanguage] = useState<Language>("CPP");
    const [submittedCode, setSubmittedCode] = useState("");
    const [submitting, setSubmitting] = useState(false);
    const [evaluationResult, setEvaluationResult] = useState<EvaluationResult | null>(null);

    useEffect(() => {
        let cancelled = false;

        (async () => {
            setError(null);
            setWaking(true);

            try {
                await Api.wakeBackend();
                if (!cancelled) setBackendReady(true);
            } catch (e) {
                console.error(e);
                if (!cancelled) setError("Waking server failed... Please refresh in a moment.");
            } finally {
                if (!cancelled) setWaking(false);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, []);

    useEffect(() => {
        if (!backendReady) return;

        if (!problemId) {
            setError("No problem ID provided.");
            return;
        }

        let cancelled = false;

        (async () => {
            setError(null);
            setLoadingData(true);

            try {
                const [problemInfo, stats] = await Promise.all([
                    Api.getProblemById(problemId),
                    Api.getStats(),
                ]);

                if (cancelled) return;
                setProblem(problemInfo);
                setXp(stats);
            } catch (e) {
                console.error(e);
                
                if (!cancelled) setError("Loading data failed... Please refresh in a moment.");
            } finally {
                if (!cancelled) setLoadingData(false);
            }
        })();

        return () => {
            cancelled = true;
        };
    }, [backendReady, problemId]);

    if (error) {
        return(
            <LoadingPage message={error} showRetry/>
        );
    }

    const showLoading = waking || (backendReady && loadingData);
    if (showLoading) {
        return <LoadingPage />;
    }

    const handleSubmit = async () => {
        if (!problemId) return;

        setSubmitting(true);
        setError(null);

        try {
            const body: EvaluationRequest = {
                problemId: problemId,
                submittedCode,
                language,
            };

            const result = await Api.evaluateProblem(problemId, body);
            setEvaluationResult(result);

            const stats = await Api.getStats();
            setXp(stats);
        } catch (err) {
            console.error(err);
            setError("Failed to evaluate your code.");
        } finally {
            setSubmitting(false);
        }
    };

    const handleRunCode = () => {
        console.log("Run clicked with code: ", submittedCode)
    }

    return (
        <motion.div
            custom={direction}
            variants={variants}
            initial="initial"
            animate="animate"
            exit="exit"
            transition={{ duration: 2, ease: "easeInOut" }}
            style={{
                position: "absolute",
                inset: 0,
                width: "100%",
                height: "100%",
                overflow: "hidden"
            }}
        >
            <Split
                className="problem-page"
                sizes={[20, 55, 25]}
                minSize={[200, 300, 200]}
                gutterSize={8}
            >
                <div className="description-panel">
                    {error && <p className="error-message">{error}</p>}

                    <button className="back-button" onClick={() => {
                        setDirection(-1);
                        navigate("/problems");
                    }}>
                        {"BACK"}
                    </button>

                    <h1 className="problem-title bobbing-title">{problem?.title}</h1>
                    <pre style= {{ whiteSpace: "pre-wrap" }}>
                        <p className="problem-text">{problem?.description}</p>
                    </pre>
                    
                    <h2 className="section-title bobbing-title">EXAMPLES</h2>
                    <pre style= {{ whiteSpace: "pre-wrap" }}>
                        <p className="problem-text">{problem?.examples}</p>
                    </pre>

                    <h2 className="section-title bobbing-title">CONSTRAINTS</h2>
                    <pre style= {{ whiteSpace: "pre-wrap" }}>
                        <p className="problem-text">{problem?.constraints}</p>
                    </pre>
                </div>

                <Split
                    className="code-panel"
                    direction="vertical"
                    sizes={[75, 25]}
                    minSize={[300, 100]}
                    gutterSize={6}
                >
                    <EditorSection 
                        language={language}
                        code={submittedCode}
                        onLanguageChange={setLanguage}
                        onCodeChange={setSubmittedCode}
                        onRun={handleRunCode}
                        onSubmit={handleSubmit}
                    />

                    <div className="output">
                        <h3 className="bobbing-title">{submitting ? "EVALUATING..." : "OUTPUT"}</h3>

                        {!evaluationResult && !error && (
                            <p className="output-line">
                                Submit your code to see results here.
                            </p>
                        )}

                        {evaluationResult && (
                            <>
                                <p className="output-line">
                                    {evaluationResult.passed
                                        ? "All test cases passed"
                                        : "Some test cases failed"}
                                </p>

                                {!evaluationResult.passed && 
                                evaluationResult.failedTestCaseIndices.length > 0 && (
                                    <p className="output-line">
                                        Failed test indices: {" "}
                                        {evaluationResult.failedTestCaseIndices.join(", ")}
                                    </p>
                                )}

                                {evaluationResult.stdout && (
                                    <>
                                        <h4 className="standard-output">Standard Output:</h4>
                                        <pre className="output-block">{evaluationResult.stdout}</pre>
                                    </>
                                )}

                                {evaluationResult.stderr && (
                                    <>
                                        <h4 className="standard-error">Standard Error:</h4>
                                        <pre className="output-block">{evaluationResult.stderr}</pre>
                                    </>
                                )}
                            </>
                        )}

                        {error && (
                            <p className="output-line error-message">{error}</p>
                        )}
                    </div>
                </Split>

                <div className="lune-panel">

                    <div className="profile-box">

                        <p className="char-name">(NOT YET) LUNE</p>

                        <div className="sprite-wrapper">
                            <img src={tempBg} alt="Lune BG" className="tempBg" />
                            <img src={tempSprite} alt="Mew Sprite (Temp)" className="tempSprite" />
                        </div>

                        <div className="stats">
                            <div className="level">
                                <p>LVL {xp ? xp.newLevel : "?"}</p>
                            </div>

                            <div className="streak">
                                <span className="stat-label">STREAK </span>
                                <span className="streak-value">{xp ? xp.streak : "?"} </span>
                            </div>
                            
                            <div className="stat-row">
                                <span className="stat-label">HP</span>
                                <div className="hp-bar" />
                            </div>

                            <div className="stat-row">
                                <span className="stat-label">XP</span>
                                <div className="xp-bar">
                                    <div
                                        className="xp-fill"
                                        style={{
                                            width: xp ? `${Math.min(xp.currentXp % 100, 100)}%` : "0%",
                                        }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </Split>
        </motion.div>
    );
};

export default ProblemPage;