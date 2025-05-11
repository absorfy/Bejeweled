import {createContext, useContext, useEffect, useState} from "react";
import gsAxios from "../api";


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

  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const response = await gsAxios.get("/player/status")
        console.log(response.data)
        setPlayerLogin(response.data)
      } catch (error) {
        setPlayerLogin(null)
      }
    }

    fetchStatus();
  }, [])

  return (
    <PlayerContext.Provider value={{playerLogin, setPlayerLogin}}>
      {children}
    </PlayerContext.Provider>
  )
}
