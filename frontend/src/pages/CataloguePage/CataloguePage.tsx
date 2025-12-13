import { useEffect, useState } from "react";
import { Api } from "../../api";
import { useNavigate } from "react-router-dom";
import { type Tag, type Difficulty, type Problem, type Xp } from "../../types/index";
import "./CataloguePage.css";
import tempBg from "../../assets/temp-lune-bg.jpeg";
import tempSprite from "../../assets/mew.gif";
import { motion } from "framer-motion";

const difficultyOptions: Difficulty[] = ["EASY", "MEDIUM", "HARD"];
const tagOptions: Tag[] = [ "ARRAY", "DP", "STRING", "HASHMAP", "TWO_POINTERS", "BINARY_SEARCH", "TREE", "GRAPH", "DFS", "BFS", "GREEDY", "TRIE", "STACK", "HASHSET", "UNION_FIND", "SORTING"];

const variants = {
    initial: (direction: number) => ({ x: direction > 0 ? "100%" : "-100%", opacity: 0 }),
    animate: { x: 0, opacity: 1 },
    exit: (direction: number) => ({ x: direction > 0 ? "-100%" : "100%", opacity: 0 }),
};

type CataloguePageProps = {
    direction: number;
    setDirection: (dir: number) => void;
}

const CataloguePage: React.FC<CataloguePageProps> = ({ direction, setDirection }) => {
    const navigate = useNavigate();

    const [problems, setProblems] = useState<Problem[]>([]);
    const [xp, setXp] = useState<Xp | null>(null);

    const [selectedDiffs, setSelectedDiffs] = useState<Difficulty[]>([]);
    const [selectedTags, setSelectedTags] = useState<Tag[]>([]);

    const [, setError] = useState<string | null>(null);
    
    const toggleItem = <T extends string>(
        value: T,
        list: T[],
        setList: (v: T[]) => void
    ) => {
        if (list.includes(value)) {
            setList(list.filter((item) => item !== value));
        } else {
            setList([...list, value]);
        }
    };

    useEffect(() => {
        setError(null);

        Api.getProblems({
            tags: selectedTags.length > 0 ? selectedTags : undefined,
            difficulties: selectedDiffs.length > 0 ? selectedDiffs : undefined,
        })
            .then(setProblems)
            .catch((error) => {
                console.error(error);
                setError("Failed to fetch problems.");
            })
        
        Api.getStats()
            .then((stats) => setXp(stats))
            .catch((err) => {
                console.error(err);
                setError("Failed to fetch user stats.");
            })
    }, [selectedTags, selectedDiffs]);

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
            <div className="catalogue-container">

                <div className="left-panel">
                    <div className="filter-bar">

                        {/* DIFFICULTY FILTERS */}
                        <div className="filter-group" style={{ marginBottom: "18px" }}>
                            <span className="filter-label">DIFFICULTY:</span>

                            <div className="filter-options">
                                {difficultyOptions.map((diff) => (
                                    <label key={diff} className="checkbox-wrapper">
                                    <input
                                        type="checkbox"
                                        checked={selectedDiffs.includes(diff)}
                                        onChange={() => 
                                            toggleItem<Difficulty>(diff, selectedDiffs, setSelectedDiffs)
                                        }
                                    />
                                    <span className={`filter-text ${diff.toLowerCase()}`}>{diff}</span>
                                    </label>
                                ))}
                            </div>
                        </div>
                        
                        {/* TAG FILTERS */}
                        <div className="filter-group">
                            <span className="filter-label">CATEGORIES:</span>
                            
                            <div className="filter-options">
                                {tagOptions.map((tag) => (
                                    <label key={tag} className="checkbox-wrapper">
                                    <input
                                        type="checkbox"
                                        checked={selectedTags.includes(tag)}
                                        onChange={() => 
                                            toggleItem<Tag>(tag, selectedTags, setSelectedTags)
                                        }
                                    />
                                    <span className="filter-text">{tag}</span>
                                    </label>
                                ))}
                            </div>
                        </div>
                    </div>

                    <ul className="problem-list">
                        {problems.map((problem) => (
                            <li key={problem.id}>
                                <button className="problem-item" onClick={() => {
                                    setDirection(1);
                                    navigate(`/problems/${problem.id}`);
                                }}>
                                    {problem.title}
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>

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
            </div>
        </motion.div>
    );
};

export default CataloguePage;