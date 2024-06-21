import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';
import Materia from './pages/Materia'; 
import Assunto from './pages/Assunto'; 

import "./assets/main.css";

function App() {
  return (
    <Router>
      <div className="h-screen overflow-y-auto flex flex-col bg-cover bg-center" style={{ backgroundImage: "url('/fundo.jpg')" }}>
        <Header />
        <div className="flex-grow">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/Materia" element={<Materia />} />
            <Route path="/Assunto" element={<Assunto />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
