import logo from './logo.svg';
import './App.css';
import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MapPage from "routers/MapPage";
import ListPage from "routers/ListPage";
import Detail from "routers/Detail";

function App() {
  return (
      <BrowserRouter>
        <Navigation />
        <Routes>
          <Route path="/map" element={<MapPage />}></Route>
          <Route path="/list" element={<ListPage />}></Route>
          {/*<Route path="/:page" element={<Home />} />*/}
          <Route path="/detail" element={<Detail />} />
        </Routes>
      </BrowserRouter>
  );
}

export default App;
