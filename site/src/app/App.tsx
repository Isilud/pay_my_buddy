"use client";

import Header from "./layout/Header";
import "./App.scss";
import { BrowserRouter } from "react-router-dom";
import Main from "./layout/Main";
import { RecoilRoot } from "recoil";

export default function App(): JSX.Element {
  return (
    <div className="app">
      <RecoilRoot>
        <BrowserRouter>
          <Header />
          <Main />
        </BrowserRouter>
      </RecoilRoot>
    </div>
  );
}
