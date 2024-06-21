import { useRecoilState } from "recoil";
import { userDataState } from "../store/userAtom";
import "./ContactPage.scss";
import { useEffect, useState } from "react";
import User from "../types/User";
import { getCurrentUser } from "../requests/dataRequest";

export default function HomePage(): JSX.Element {
  const [userData, setUserData] = useRecoilState(userDataState);
  const [userInfo, setUserInfo] = useState<User | null>(null);

  useEffect(() => updateUserInfo(), []);

  function updateUserInfo(): void {
    if (userData)
      getCurrentUser(userData).then((res) => {
        setUserInfo(res);
      });
  }

  if (!userInfo) return <div className="contactpage">Chargement</div>;

  return <div className="contactpage">Bienvenue {userInfo.name} !</div>;
}
