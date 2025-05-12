
import buttonStyles from '../../Button.module.css'
import { motion } from "framer-motion";

export default function HintButton({ hintCount, hintHandler, isDisabled }) {
  const disable = hintCount <= 0 || isDisabled;
  return (
      <motion.button
        className={buttonStyles.defaultButton}
        onClick={hintHandler}
        disabled={disable}
        whileHover={!disable ? { scale: 1.05 } : {}}
        whileTap={!disable ? { scale: 0.95 } : {}}
        animate={{ opacity: disable ? 0.5 : 1 }}
        transition={{ type: "spring", stiffness: 300 }}
      >
        {hintCount > 0 ? `Hint ${hintCount}` : 'No Hints'}
      </motion.button>
  );
}
