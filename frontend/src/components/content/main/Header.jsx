import React, {useState} from 'react';
import { useLocation } from "react-router-dom";
import LoginModal from "./LoginModal";
import {usePlayer} from "../../PlayerContext";
import gsAxios from "../../../api";
import styles from "./Header.module.css";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";

function Header() {
  const [showLoginModal, setShowLoginModal] = useState(false);
  const {playerLogin, setPlayerLogin} = usePlayer()
  const navigate = useNavigate();

  const handleLogout = async () => {
    await gsAxios.post("/player/logout");
    setPlayerLogin(null);
  };

  const isHome = useLocation().pathname === '/';

  return (
    <>
      <nav className={`${styles.header} navbar navbar-dark px-4`}>
        <div className="container-fluid d-flex justify-content-between align-items-center position-relative">
          <div>
            {!isHome && (
              <motion.button
                className={styles.headerButton}
                onClick={() => navigate("/")}
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                transition={{ type: "spring", stiffness: 300 }}
              >
                ← Back
              </motion.button>
            )}
          </div>

          <div className={styles.headerCenter}>
            <span className={styles.headerTitle}>GameStudio</span>
          </div>

          <div className="d-flex align-items-center gap-2">
            {playerLogin ? (
              <>
                <span className="navbar-brand me-2">{playerLogin}</span>
                <motion.button
                  className={styles.headerButton}
                  onClick={handleLogout}
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  transition={{ type: "spring", stiffness: 300 }}
                >
                  Log Out
                </motion.button>
              </>
            ) : (
              <motion.button
                className={styles.headerButton}
                onClick={() => setShowLoginModal(true)}
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                transition={{ type: "spring", stiffness: 300 }}
              >
                Log In
              </motion.button>
            )}
          </div>
        </div>
      </nav>

      <LoginModal isOpen={showLoginModal} onClose={() => setShowLoginModal(false)} />
    </>
  );
}

export default Header;
