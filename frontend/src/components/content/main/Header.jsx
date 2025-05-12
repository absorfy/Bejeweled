import React, {useState} from 'react';
import { useLocation } from "react-router-dom";
import LoginModal from "./LoginModal";
import {usePlayer} from "../../PlayerContext";
import gsAxios from "../../../api";
import styles from "./Header.module.css";
import { useNavigate } from "react-router-dom";
import DefaultButton from "../../DefaultButton";

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
              <DefaultButton buttonClickHandler={() => navigate("/")} textValue={"← Back"} />
            )}
          </div>

          <div className={styles.headerCenter}>
            <span className={styles.headerTitle}>GameStudio</span>
          </div>

          <div className="d-flex align-items-center gap-2">
            {playerLogin ? (
              <>
                <span className="navbar-brand me-2">{playerLogin}</span>
                <DefaultButton buttonClickHandler={handleLogout} textValue={"Log Out"} />
              </>
            ) : (
              <DefaultButton buttonClickHandler={() => setShowLoginModal(true)} textValue={"Log In"}/>
            )}
          </div>
        </div>
      </nav>

      <LoginModal isOpen={showLoginModal} onClose={() => setShowLoginModal(false)} />
    </>
  );
}

export default Header;
