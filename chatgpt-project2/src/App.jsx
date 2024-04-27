import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import Materia from './pages/Materia'; 
import Assunto from './pages/Assunto'; 
import "./assets/main.css";

function App() {
  return (
    <Router>
      <div className="bg-base-200 flex flex-col">
        <Routes>
				<Route path="/" element={<Home />} />
        <Route path="/Materia" element={<Materia />} />
        <Route path="/Assunto" element={<Assunto />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
