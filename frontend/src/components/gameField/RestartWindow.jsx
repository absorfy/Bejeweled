import {AnimatePresence, motion} from "framer-motion";
import bejeweledStyles from "./BejeweledField.module.css";
import {startGame} from "../../api/bejeweled.service";


export default function RestartWindow({currentField, setCurrentField, setFieldTiles, setLoading, setScoreSent}) {

  function handleRestart() {
    setLoading(true);
    startGame()
      .then(res => {
        setScoreSent(false)
        setCurrentField(res.data);
        setFieldTiles(res.data.tiles.flat().map(tile => ({
          ...tile,
          isNew: true,
          isStart: true,
        })));
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }

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
            <button onClick={handleRestart}>New game</button>
          </motion.div>
        </motion.div>
      )}
    </AnimatePresence>
  )
}
