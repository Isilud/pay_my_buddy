import { useState } from "react";
import Logo from "../component/Logo";
import "./ConnexionPage.scss";
import {
  FormAuthentication,
  userAuthentication,
} from "../requests/authRequests";
import { useRecoilState } from "recoil";
import { UserData, userDataState } from "../store/userAtom";
import { setLocalUserData } from "../requests/localStorage";

export default function ConnexionPage(): JSX.Element {
  const [_, setUserData] = useRecoilState(userDataState);

  const [form, setForm] = useState<FormAuthentication>({
    email: "",
    password: "",
  });

  function handleFormChange(newValue: Partial<FormAuthentication>) {
    setForm({ ...form, ...newValue });
  }

  return (
    <div className="connexionpage">
      <div className="connexionpage__form">
        <Logo />
        <input
          className="connexionpage__input"
          type="text"
          placeholder="Email"
          value={form.email}
          onChange={(e) => {
            handleFormChange({ email: e.target.value });
          }}
        />
        <input
          className="connexionpage__input"
          type="password"
          placeholder="Mot de passe"
          value={form.password}
          onChange={(e) => {
            handleFormChange({ password: e.target.value });
          }}
        />
        <button
          className="connexionpage__button"
          onClick={() => {
            userAuthentication(form).then((res) => {
              setLocalUserData(res);
              setUserData(res);
            });
          }}
        >
          Login
        </button>
      </div>
    </div>
  );
}
