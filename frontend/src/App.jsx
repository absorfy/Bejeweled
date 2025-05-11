import 'bootstrap/dist/css/bootstrap.min.css';
import {Route, Routes} from "react-router-dom";
import './App.css';
import BejeweledField from "./components/gameField/BejeweledField";
import OtherGame from "./components/content/game/OtherGame";
import Footer from "./components/content/main/Footer";
import Header from "./components/content/main/Header";
import GameContent from "./components/content/game/GameContent";
import Index from "./components/content/main";

function App() {
    return (
      <div className="content app-container d-flex flex-column min-vh-100 mx-auto">
        <Header/>
        <main className="flex-grow-1">
          <Routes>
            <Route path={"/"} element={<Index/>} />
            <Route path={"/bejeweled"} element={<GameContent gameName="bejeweled" gameComponent={<BejeweledField/>} />} />
            <Route path={"/otherGame"} element={<OtherGame/>} />
          </Routes>
        </main>
        <Footer/>
      </div>
    );
}

export default App;
