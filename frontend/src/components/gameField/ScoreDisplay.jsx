import { motion, AnimatePresence } from "framer-motion";
import styles from "./ScoreDisplay.module.css";
import {useEffect, useState} from "react";

let incrementId = 0
let comboId = 0

export default function ScoreDisplay({ score, lastIncrement, chainCombo }) {
  const [increments, setIncrements] = useState([])
  const [combos, setCombos] = useState([])

  useEffect(() => {
    if (lastIncrement > 0) {
      const id = incrementId++;
      setIncrements((prev) => [...prev, { id, value: lastIncrement }]);

      setTimeout(() => {
        setIncrements((prev) => prev.filter((increment) => increment.id !== id));
      }, 1000);
    }
  }, [lastIncrement]);

  useEffect(() => {
    if (chainCombo > 0) {
      const id = comboId++;
      setCombos((prev) => [...prev, {id, value: `Combo ${chainCombo}`}]);

      setTimeout(() => {
        setCombos((prev) => prev.filter((combo) => combo.id !== id));
      }, 1000);
    }
  }, [chainCombo]);

  return (
    <div className={styles.scoreDisplay}>
      <div className={styles.container}>
        <span className={styles.label}>Score:</span>
        <AnimatePresence mode="wait">
          <motion.span
            key={score}
            initial={{ y: -10, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            exit={{ y: 10, opacity: 0 }}
            transition={{ duration: 0.3 }}
            className={styles.score}
          >
            {score}
          </motion.span>
        </AnimatePresence>
      </div>
      {increments.map((increment) => (
        <motion.div
          key={increment.id}
          initial={{ y: 0, opacity: 1 }}
          animate={{ y: 30, opacity: 0 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 1 }}
          className={styles.increment}
        >
          +{increment.value}
        </motion.div>
      ))}
      {combos.map((combo) => (
        <motion.div
          key={combo.id}
          initial={{ y: 0, opacity: 1 }}
          animate={{ y: 30, opacity: 0 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 1 }}
          className={styles.combo}
        >
          {combo.value}
        </motion.div>
      ))}
    </div>
  );
}
