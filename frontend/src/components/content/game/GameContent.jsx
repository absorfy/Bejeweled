import GameTabs from "./GameTabs";
import styles from "./GameContent.module.css";
import {useEffect, useState} from "react";

export default function GameContent({gameName, gameComponent}) {
  const [backdropIndex, setBackdropIndex] = useState(0);

  useEffect(() => {
    setBackdropIndex(Math.floor(Math.random() * 5))
  }, []);

  const backdrops = [
    styles.backdrop0,
    styles.backdrop1,
    styles.backdrop2,
    styles.backdrop3,
    styles.backdrop4
  ]

  return (
    <div className={`${styles.gameBackdrop} ${backdrops[backdropIndex]}`}>
      <div className={styles.contentWrapper}>
        <div className={styles.gameComponent}>
          {gameComponent}
        </div>
        <div className={styles.gameTabs}>
          <GameTabs gameName={gameName} />
        </div>
      </div>
    </div>
  )
}
