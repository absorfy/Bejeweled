import { motion, AnimatePresence } from "framer-motion";
import styles from "./LoginModal.module.css";


export default function ModalWindow({children, isOpen, onClose }) {

  const dialogInitialState = {
    opacity: 0,
    filter: "blur(10px)",
    rotateY: 25,
    rotateX: 5,
    z: -100,
    transformPerspective: 500,
    transition: { duration: 0.3 }
  };

  const dialogOpenState = {
    opacity: 1,
    filter: "blur(0px)",
    rotateX: 0,
    rotateY: 0,
    z: 0,
    transition: { duration: 0.4 }
  };


  return (
    <AnimatePresence>
      {isOpen && (
        <>
          <motion.div
            className={styles.overlay}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            onClick={onClose}
          />
          <motion.div
            className={styles.modalContainer}
            initial={dialogInitialState}
            animate={dialogOpenState}
            exit={dialogInitialState}
            style={{ transformPerspective: 500 }}
          >
            {children}
          </motion.div>
        </>
      )}
    </AnimatePresence>
  );
}
