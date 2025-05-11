import styles from "./Gem.module.css";

export default function LockTile({lockTile}) {
  return (
    <div className={styles.gemContainer}>
      <span style={{color: lockTile.gem.color, fontSize: 20}}>{lockTile.needBreakCount}</span>
    </div>
  )
}
