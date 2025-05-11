import React, {useState} from 'react';
import { Link, useLocation } from "react-router-dom";
import LoginModal from "./LoginModal";
import {usePlayer} from "./PlayerContext";
import gsAxios from "../../api";

function Header() {
  const [showLoginModal, setShowLoginModal] = useState(false);
  const {playerLogin, setPlayerLogin} = usePlayer()


  const handleLogout = async () => {
    await gsAxios.post("/player/logout");
    setPlayerLogin(null);
  };

  const isHome = useLocation().pathname === '/';

  return (
    <>
      <nav className="navbar navbar-dark bg-dark px-4">
        <div className="container-fluid d-flex justify-content-between align-items-center">
          <div>
            {!isHome && (
              <Link to="/" className="btn btn-outline-light me-3">← Back</Link>
            )}
          </div>
          <span className="navbar-brand mx-auto fs-4">GameStudio</span>
          {playerLogin ? (
            <>
              <span className="navbar-brand me-2">{playerLogin}</span>
              <button className="btn btn-outline-danger" onClick={handleLogout}>Log Out</button>
            </>
          ) : (
            <button className="btn btn-outline-info" onClick={() => setShowLoginModal(true)}>Log In</button>
          )}
        </div>
      </nav>

      <LoginModal isOpen={showLoginModal} onClose={() => setShowLoginModal(false)} />
    </>
  );
}

export default Header;
