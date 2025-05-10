import React from 'react';
import {NavLink} from "react-router-dom";

const Index = () => (
  <>
    Our favorite games:<br/>
    <ol>
      <li><NavLink to="/bejeweled">Bejeweled</NavLink></li>
      <li><NavLink to="/otherGame">Other game</NavLink></li>
    </ol>
  </>
);

export default Index;
