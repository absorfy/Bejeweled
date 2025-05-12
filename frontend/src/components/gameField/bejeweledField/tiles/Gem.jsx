import { motion } from "framer-motion";
import GemWithEffect from "./GemWithEffect";

export default function Gem({ gem, handleDragEnd, setDirection, index, animating }) {
  const row = Math.floor(index / 8);
  const fallDelay = 0.05;
  const fallDuration = fallDelay * 8 + (gem?.isStart ? 1 : 0);

  gem.isNew = gem.impact !== 'NONE' ? false : gem.isNew;

  const isAppearing = gem.justAppeared;
  const isDisappearing = gem.isDisappearing;

  return (
    <motion.li
      layout
      initial={{
        y: gem.isNew && !isAppearing ? (-row - 4) * 60 : 0,
        opacity: isAppearing ? 0 : (gem.isNew ? 0 : 1),
        scale: isAppearing ? 0.6 : 1,
      }}
      animate={{
        y: 0,
        opacity: isDisappearing ? 0 : 1,
        scale: isDisappearing ? 0.4 : 1,
      }}

      transition={{
        layout: {
          type: "spring",
          stiffness: 400,
          damping: 25,
          delay: (Math.random() / 2) * fallDelay,
        },
        y: {
          type: "spring",
          stiffness: 120,
          damping: 18,
          delay: isAppearing ? 0 : fallDuration - row * fallDelay + (Math.random() - 0.5) * fallDelay,
        },
        opacity: { duration: 0.4 },
        scale: { duration: 0.4 },
      }}
      drag={!animating}
      dragDirectionLock={!animating}
      onDirectionLock={!animating ? setDirection : undefined}
      dragConstraints={{ top: 0, right: 0, bottom: 0, left: 0 }}
      dragTransition={{ bounceStiffness: 1000, bounceDamping: 40 }}
      dragElastic={0.1}
      whileDrag={{ cursor: "grabbing" }}
      onDragEnd={!animating ? (event, info) => handleDragEnd(event, info, index) : undefined}
    >
      <GemWithEffect gem={gem} />
    </motion.li>
  );
}
