import { useState } from "react";
import { FormRegistration, userRegistration } from "../requests/authRequests";
import Logo from "../component/Logo";
import "./ConnexionPage.scss";
import { useNavigate } from "react-router-dom";

export default function InscriptionPage(): JSX.Element {
  const navigate = useNavigate();
  const [form, setForm] = useState<FormRegistration>({
    email: "",
    firstName: "",
    lastName: "",
    password: "",
    account: {
      code: "",
    },
  });

  function handleFormChange(newValue: Partial<FormRegistration>) {
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
          type="text"
          placeholder="Mot de passe"
          value={form.password}
          onChange={(e) => {
            handleFormChange({ password: e.target.value });
          }}
        />
        <input
          className="connexionpage__input"
          type="text"
          placeholder="Prénom"
          value={form.firstName}
          onChange={(e) => {
            handleFormChange({ firstName: e.target.value });
          }}
        />
        <input
          className="connexionpage__input"
          type="text"
          placeholder="Nom"
          value={form.lastName}
          onChange={(e) => {
            handleFormChange({ lastName: e.target.value });
          }}
        />
        <input
          className="connexionpage__input"
          type="text"
          placeholder="Numéro de compte"
          value={form.account.code}
          onChange={(e) => {
            handleFormChange({ account: { code: e.target.value } });
          }}
        />
        <button
          className="connexionpage__button"
          onClick={() => {
            userRegistration(form).then(() => navigate("/"));
          }}
        >
          Register
        </button>
      </div>
    </div>
  );
}
