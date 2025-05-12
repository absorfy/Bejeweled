import styles from "./Gem.module.css";
import GemImage from "./GemImage";
import { useAnimation, motion } from "framer-motion";
import { useEffect } from "react";

export default function LockTile({ lockTile }) {
  const controls = useAnimation();
  const isAppearing = lockTile?.isStart;
  const { scale, opacity } = getScaleAndOpacity(lockTile.needBreakCount);

  useEffect(() => {
    controls.start({
      scale: [1, 1.05, 0.98, 1.03, 0.96, scale],
      x: [0, -2, 2, -1, 1, 0],
      y: [0, 2, -2, 1, -1, 0],
      opacity,
      transition: {
        duration: 0.6,
        ease: "easeInOut",
      },
    });
  }, [lockTile.needBreakCount]);

  function getScaleAndOpacity(level) {
    switch (level) {
      case 3: return { scale: 0.85, opacity: 0.9 };
      case 2: return { scale: 0.7, opacity: 0.75 };
      case 1: return { scale: 0.55, opacity: 0.5 };
      default: return { scale: 0, opacity: 0 };
    }
  }

  return (
    <motion.div
      className={styles.gemContainer}
      initial={isAppearing ? { scale: 0.6, opacity: 0 } : {}}
      animate={isAppearing ? { scale: 1, opacity: 1 } : {}}
      transition={
        isAppearing
          ? {
            duration: 0.4,
            delay: 1 + Math.random() * 0.5,
            ease: "easeOut",
          }
          : {}
      }
    >
      <GemImage color={lockTile.gem.color} />

      <div className={styles.lockWrapper}>
        <motion.div
          className={styles.lockOverlay}
          animate={controls}
          initial={getScaleAndOpacity(lockTile.needBreakCount)}
        />
      </div>
    </motion.div>
  );
}
