import React from 'react';
import ReactDOM from 'react-dom/client';
import './main.css';
import App from './App';
import {BrowserRouter} from "react-router-dom";
import {PlayerProvider} from "./components/index/PlayerContext";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <BrowserRouter>
    <PlayerProvider>
      <App />
    </PlayerProvider>
  </BrowserRouter>
);

