import {motion} from "framer-motion";
import GemWithEffect from "./GemLottie";



export default function Gem({gem, handleDragEnd, setDirection, index}) {
  const row = Math.floor(index / 8);
  const fallDelay = 0.1
  const fallDuration = fallDelay * 8

  gem.isNew = gem.impact !== 'NONE' ? false : gem.isNew

  return (
    <motion.li
      layout

      initial={{ y: gem.isNew ? (-row - 4) * 70 : 0, opacity: gem.isNew ? 0 : 1 }}
      animate={{ y: 0, opacity: 1 }}
      transition={{
        layout: { type: "spring", stiffness: 400, damping: 25, delay: (Math.random() / 2) * fallDelay },
        y: { type: "spring", stiffness: 120, damping: 18, delay: fallDuration - row * fallDelay + (Math.random() - 0.5) * fallDelay },
        opacity: { duration: 1 },
      }}

      drag
      dragDirectionLock
      onDirectionLock={setDirection}
      dragConstraints={{ top: 0, right: 0, bottom: 0, left: 0 }}
      dragTransition={{ bounceStiffness: 1000, bounceDamping: 40 }}
      dragElastic={0.2}
      whileDrag={{ cursor: "grabbing" }}
      onDragEnd={(event, info) => handleDragEnd(event, info, index)}
    >

      <GemWithEffect gem={gem} />
    </motion.li>
  )
}
