import {createContext, useContext, useEffect, useState} from "react";


export const PlayerContext = createContext(null);
export const usePlayer = () => useContext(PlayerContext)

export const PlayerProvider = ({children}) => {
  const [playerLogin, setPlayerLogin] = useState(() => {
    return localStorage.getItem("playerLogin");
  });

  useEffect(() => {
    if (playerLogin) {
      localStorage.setItem("playerLogin", playerLogin);
    } else {
      localStorage.removeItem("playerLogin");
    }
  }, [playerLogin]);

  return (
    <PlayerContext.Provider value={{playerLogin, setPlayerLogin}}>
      {children}
    </PlayerContext.Provider>
  )
}
