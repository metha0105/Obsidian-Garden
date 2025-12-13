import './StartPage.css';
import logo from '../../assets/logo.png';
import React from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

const variants = {
  initial: (direction: number) => ({ x: direction > 0 ? "100%" : "-100%", opacity: 0 }),
  animate: { x: 0, opacity: 1 },
  exit: (direction: number) => ({ x: direction > 0 ? "-100%" : "100%", opacity: 0 }),
};

type StartPageProps = {
  direction: number;
  setDirection: (dir: number) => void;
}

const StartPage: React.FC<StartPageProps> = ({ direction, setDirection }) => {
  const navigate = useNavigate();

  return (
    <motion.div
      custom={direction}
      variants={variants}
      initial="intial"
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
      <div 
        className="start-container"
        style={{
          backgroundImage: "url(/start-bg.gif)",
          backgroundSize: "cover",
          backgroundPosition: "center",
          minHeight: "100vh"
        }}
      >
        <img src={logo} alt="Obsidian Garden logo" className="logo-img" />
        <button className="start-button" onClick={() => {
          setDirection(1);
          navigate("/problems");
        }}>
          START
        </button>
      </div>
    </motion.div>
  );
};

export default StartPage;