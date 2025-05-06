import styles from './Gem.module.css';
import {motion} from "framer-motion";
import {gemEffects} from "./effects/gemEffects";



export default function Gem({gem, onGemClick}) {

  return (
    <motion.div
      onClick={onGemClick}
      className={styles.gemContainer}
      animate={gemEffects[gem.impact].animation}
      transition={gemEffects[gem.impact].transition}
      style={gemEffects[gem.impact].style}
    >
      <img
        src={`/gemImages/${gem.color}_gem.png`}
        alt="gemIcon"
        className={styles.gemImage}
      />
    </motion.div>
  )
}
