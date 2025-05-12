import styles from './HintButton.module.css'
import { motion } from "framer-motion";

export default function HintButton({ hintCount, hintHandler, isEnable }) {
  const isDisabled = hintCount <= 0 || !isEnable;
  return (
    <div className={styles.hintButtonContainer}>
      <motion.button
        className={styles.hintButton}
        onClick={hintHandler}
        disabled={isDisabled}
        whileHover={!isDisabled ? { scale: 1.05 } : {}}
        whileTap={!isDisabled ? { scale: 0.95 } : {}}
        animate={{ opacity: isDisabled ? 0.5 : 1 }}
        transition={{ type: "spring", stiffness: 300 }}
      >
        {hintCount > 0 ? `Hint ${hintCount}` : 'No Hints'}
      </motion.button>
    </div>
  );
}
