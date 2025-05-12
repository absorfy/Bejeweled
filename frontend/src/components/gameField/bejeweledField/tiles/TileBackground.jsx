import styles from '../BejeweledField.module.css';
import { motion } from "framer-motion";

export default function TileBackground({ tile, rowIndex, colIndex }) {
  const tileType =
    tile.tileName === 'AirTile'
      ? styles.airTile
      : (rowIndex + colIndex) % 2 === 0
        ? styles.tileType1
        : styles.tileType2;

  const delay = 0.1 * rowIndex + 0.05 * colIndex;

  return (
    <motion.div
      initial={tile.isStart ? { y: -100, opacity: 0, scale: 0.6 } : {}}
      animate={tile.isStart ? { y: 0, opacity: 1, scale: 1 } : {}}
      transition={
        tile.isStart
          ? {
            delay,
            duration: 0.5,
            ease: [0.2, 0.8, 0.2, 1],
            type: "spring",
            stiffness: 120,
            damping: 8,
          }
          : {}
      }
      onAnimationComplete={() => {
        if(tile.isStart) {
          tile.isStart = false;
        }
      }}

      key={"TileContent" + colIndex}
      className={`${styles.tile} ${tileType} ${tile.hint ? styles.hint : ''}`}
      style={{
        top: `${rowIndex * 60}px`,
        left: `${colIndex * 60}px`,
        position: 'absolute'
      }}
    />
  );
}
