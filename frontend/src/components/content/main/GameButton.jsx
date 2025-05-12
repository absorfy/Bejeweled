import {NavLink} from "react-router-dom";
import {motion} from "framer-motion";
import styles from './GameSelector.module.css'

export default function GameButton({ to, disabled, children }) {
  const content = (
    <motion.div
      className={`${styles.gameButton} ${disabled ? styles.disabled : ""}`}
      whileHover={!disabled ? { scale: 1.05 } : {}}
      whileTap={!disabled ? { scale: 0.95 } : {}}
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.4 }}
    >
      {children}
    </motion.div>
  );

  return disabled ? (
    <div className={styles.gameButtonWrapper}>{content}</div>
  ) : (
    <NavLink to={to} className={styles.gameButtonWrapper}>
      {content}
    </NavLink>
  );
};
