import { useState } from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import { AnimatePresence } from "framer-motion";
import StartPage from "./pages/StartPage/StartPage";
import CataloguePage from "./pages/CataloguePage/CataloguePage";
import ProblemPage from "./pages/ProblemPage/ProblemPage";

export default function App() {
  const location = useLocation();

  // 1 is forward, -1 is backward
  const [direction, setDirection] = useState(1);

  return (
    <div className="app">
      <AnimatePresence mode="sync" initial={false}>
        <Routes location={location} key={location.pathname}>
          <Route path="/" element={<StartPage direction={direction} setDirection={setDirection}/>} />
          <Route path="/problems" element={<CataloguePage direction={direction} setDirection={setDirection}/>} />
          <Route path="/problems/:problemId" element={<ProblemPage direction={direction} setDirection={setDirection}/>} />
        </Routes>
      </AnimatePresence>
    </div>
  )
}