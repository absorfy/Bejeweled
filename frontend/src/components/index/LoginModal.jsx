import { motion, AnimatePresence } from "framer-motion";
import styles from "./LoginModal.module.css";
import {useState} from "react";
import handleRegister from "./LoginModalOperations";
import {usePlayer} from "./PlayerContext";

export default function LoginModal({ isOpen, onClose }) {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const { setPlayerLogin } = usePlayer();


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

  function handleSubmit(e) {
    e.preventDefault()
    handleRegister(login, password, setPlayerLogin)
      .then(() => {
        setLogin("null")
        setPassword("null")
        onClose();
      })
  }

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
            <div className={styles.modal}>
              <h2 className={styles.title}>Log In</h2>
              <form
                onSubmit={handleSubmit}
              >
                <div className="mb-3">
                  <label className="form-label">Login</label>
                  <input type="text" className="form-control" onChange={e => setLogin(e.target.value)} />
                </div>
                <div className="mb-3">
                  <label className="form-label">Password</label>
                  <input type="password" className="form-control" onChange={e => setPassword(e.target.value)} />
                </div>
                <div className="d-flex justify-content-end gap-2 mt-4">
                  <button type="button" className="btn btn-secondary" onClick={onClose}>Cancel</button>
                  <button type="submit" className="btn btn-primary">Log In</button>
                </div>
              </form>
            </div>
          </motion.div>
        </>
      )}
    </AnimatePresence>
  );
}
