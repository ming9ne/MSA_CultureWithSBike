import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import MapPage from "./routers/MapPage";
import ListPage from "./routers/ListPage";

function App() {
  return (
      <BrowserRouter>
        {/* <Navigation /> */}
        <Routes>
          <Route path="/map" element={<MapPage />}></Route>
          <Route path="/list" element={<ListPage />}></Route>
          {/*<Route path="/:page" element={<Home />} />*/}
          {/*<Route path="/detail-movie" element={<Detail />} />*/}
        </Routes>
      </BrowserRouter>
  );
}

export default App;
