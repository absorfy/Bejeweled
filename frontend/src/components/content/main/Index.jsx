import React from 'react';
import GameButton from "./GameButton";
import styles from './GameSelector.module.css';

const Index = () => (
  <div className={styles.gameSelector}>
    <GameButton to="/bejeweled">
      <img src="/images/bejeweledTitle.png" alt="BejeweledIcon" className={styles.gameIcon} />
    </GameButton>

    <GameButton disabled>
      <span>Other Game</span>
    </GameButton>
  </div>
);

export default Index;
