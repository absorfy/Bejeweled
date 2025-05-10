import 'bootstrap/dist/css/bootstrap.min.css';
import {Route, Routes} from "react-router-dom";
import './App.css';
import BejeweledField from "./components/gameField/BejeweledField";
import OtherGame from "./components/index/OtherGame";
import Footer from "./components/index/Footer";
import Header from "./components/index/Header";
import CommentsTable from "./components/CommentsTable";
import Index from "./components/index";
import ScoreTable from "./components/ScoreTable";

function App() {
    return (
      <div className="content app-container d-flex flex-column min-vh-100 mx-auto">
        <Header/>
        <main className="flex-grow-1">
          <Routes>
            <Route path={"/"} element={<Index/>} />
            <Route path={"/bejeweled"} element={<BejeweledField/>} />
            <Route path={"/otherGame"} element={<OtherGame/>} />
            <Route path={"/comments"} element={<CommentsTable gameName="bejeweled"/>} />
            <Route path={"/scores"} element={<ScoreTable gameName="bejeweled"/>} />
          </Routes>
        </main>
        <Footer/>
      </div>
    );
}

export default App;
