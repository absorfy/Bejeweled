import styles from "./Gem.module.css";


export default function GemImage({color}) {
  return (
    <img
      src={`images/gemImages/${color.toLowerCase()}_gem.png`}
      alt="gem"
      className={styles.gemImage}
      draggable={false}
    />
  )
}
