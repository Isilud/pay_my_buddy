import { Route, Routes } from "react-router-dom";
import "./Main.scss";
import ConnexionPage from "../page/ConnexionPage";
import InscriptionPage from "../page/InscriptionPage";
import { useEffect, useState } from "react";
import { useRecoilState } from "recoil";
import { PrincipalSection } from "./PrincipalSection";
import TransfertPage from "../page/TransfertPage";
import {
  deleteLocalUserData,
  getLocalUserData,
} from "../requests/localStorage";
import { UserData, userDataState } from "../store/userAtom";
import ContactPage from "../page/ContactPage";
import ProfilePage from "../page/ProfilePage";

export default function Main(): JSX.Element {
  const [userData, setUserData] = useRecoilState(userDataState);

  useEffect(() => {
    const userData = getLocalUserData();
    if (userData) setUserData(userData);
    else deleteLocalUserData();
  }, [setUserData]);

  return (
    <div className="main">
      <Routes>
        <Route path="/">
          {!userData ? (
            <>
              <Route index element={<ConnexionPage />} />
              <Route path="Inscription" element={<InscriptionPage />} />
            </>
          ) : (
            <>
              <Route
                index
                element={<PrincipalSection>Home</PrincipalSection>}
              />
              <Route
                path="Transfer"
                element={
                  <PrincipalSection>
                    <TransfertPage />
                  </PrincipalSection>
                }
              />
              <Route
                path="Profile"
                element={
                  <PrincipalSection>
                    <ProfilePage />
                  </PrincipalSection>
                }
              />
              <Route
                path="Contact"
                element={
                  <PrincipalSection>
                    <ContactPage />
                  </PrincipalSection>
                }
              />
            </>
          )}
        </Route>
      </Routes>
    </div>
  );
}
