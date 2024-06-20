import { useState } from "react";
import "./Header.scss";
import { Link, useNavigate } from "react-router-dom";
import Logo from "../component/Logo";
import { useRecoilState } from "recoil";
import { deleteLocalUserData } from "../requests/localStorage";
import { userDataState } from "../store/userAtom";

export default function Header(): JSX.Element {
  const [userData, setUserData] = useRecoilState(userDataState);

  return (
    <div className="header">
      <Logo />
      {userData ? (
        <div className="header__container">
          <Link to={"/"}>
            <button className="header__button">Accueil</button>
          </Link>
          <Link to={"/Transfer"}>
            <button className="header__button">Transfert</button>
          </Link>
          <Link to={"/Profile"}>
            <button className="header__button">Profil</button>
          </Link>
          <Link to={"/Contact"}>
            <button className="header__button">Contact</button>
          </Link>
          <Link to={"/"}>
            <button
              className="header__button"
              onClick={() => {
                deleteLocalUserData();
                setUserData(null);
              }}
            >
              Deconnexion
            </button>
          </Link>
        </div>
      ) : (
        <div className="header__container">
          <Link to={"/Inscription"}>
            <button className="header__button">Inscription</button>
          </Link>
          <Link to={"/"}>
            <button className="header__button">Connexion</button>
          </Link>
        </div>
      )}
    </div>
  );
}
