
import styles from './BejeweledField.module.css';
import {motion} from "framer-motion";

export default function SwitchTile({tile, rowIndex, colIndex}) {

  const tileType = (tile.tileName === 'AirTile') ? styles.airTile :
    ((rowIndex + colIndex) % 2 === 0 ? styles.tileType1 : styles.tileType2)

  return (
    <motion.div
      // initial={{ opacity: 0, scale: 0 }}
      // animate={{ opacity: 1, scale: 1 }}
      // transition={{
      //   delay: 0.8 - rowIndex * 0.1,
      //   duration: 0.8 - rowIndex * 0.1,
      //   scale: { type: "spring", bounce: 0.1, duration: 3.2 - rowIndex * 0.3 },
      // }}

      key={"Tile" + colIndex}
      className={`${styles.tile} ${tileType}`}
      style={{
        top: `${rowIndex * 70}px`,
        left: `${colIndex * 70}px`,
      }}
    />
  );
}
