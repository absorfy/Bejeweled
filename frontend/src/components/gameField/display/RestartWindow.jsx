import {AnimatePresence, motion} from "framer-motion";
import bejeweledStyles from "../bejeweledField/BejeweledField.module.css";
import DefaultButton from "../../DefaultButton";


export default function RestartWindow({currentField, handleRestart}) {



  return (
    <AnimatePresence>
      {currentField.fieldState === 'NO_POSSIBLE_MOVE' && (
        <motion.div
          className={bejeweledStyles.overlay}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
        >
          <motion.div
            className={bejeweledStyles.messageBox}
            initial={{ scale: 0.8, y: -50, opacity: 0 }}
            animate={{ scale: 1, y: 0, opacity: 1 }}
            exit={{ scale: 0.8, y: 50, opacity: 0 }}
            transition={{ duration: 0.4, type: "spring", bounce: 0.3 }}
          >
            <h2>Game Over</h2>
            <p>No possible swaps</p>
            <p>Your score: <strong>{currentField.score}</strong></p>
            <DefaultButton buttonClickHandler={handleRestart} textValue={"New Game"}/>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  )
}
