import React, {useState} from 'react';
import { useLocation } from "react-router-dom";
import LoginModal from "./LoginModal";
import {usePlayer} from "../../PlayerContext";
import gsAxios from "../../../api";
import styles from "./Header.module.css";
import { useNavigate } from "react-router-dom";
import DefaultButton from "../../DefaultButton";
import ModalAskWindow from "./ModalAskWindow";

function Header() {
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showAskModal, setShowAskModal] = useState(false)
  const [modalMessage, setModalMessage] = useState(null)
  const {playerLogin, setPlayerLogin} = usePlayer()
  const navigate = useNavigate();

  const handleLogout = async () => {
    await gsAxios.post("/player/logout");
    setPlayerLogin(null);
    navigate("/")
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
                <DefaultButton buttonClickHandler={() => setShowAskModal(true)} textValue={playerLogin} />
              </>
            ) : (
              <DefaultButton buttonClickHandler={() => setShowLoginModal(true)} textValue={"Log In"}/>
            )}
          </div>
        </div>
      </nav>

      <LoginModal isOpen={showLoginModal} onClose={() => setShowLoginModal(false)} setModalMessage={setModalMessage} />
      <ModalAskWindow
        isOpen={showAskModal}
        onClose={() => setShowAskModal(false)}
        textValue={"Are you sure you want to log out?" + (!isHome ? " You will be returned to the main page" : "")}
        onAccept={handleLogout}
      />
      <ModalAskWindow
        isOpen={modalMessage}
        onClose={() => setModalMessage(null)}
        textValue={modalMessage}
      />
    </>
  );
}

export default Header;
