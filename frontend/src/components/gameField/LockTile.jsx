import styles from "./Gem.module.css";
import GemImage from "./GemImage";
import { useAnimation, motion } from "framer-motion";
import { useEffect } from "react";

export default function LockTile({ lockTile }) {
  const controls = useAnimation();

  useEffect(() => {
    const { scale, opacity } = getScaleAndOpacity(lockTile.needBreakCount);

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
    <div className={styles.gemContainer}>
      <GemImage color={lockTile.gem.color} />

      <div className={styles.lockWrapper}>
        <motion.div
          className={styles.lockOverlay}
          animate={controls}
          initial={getScaleAndOpacity(lockTile.needBreakCount)}
        />
      </div>
    </div>
  );
}
