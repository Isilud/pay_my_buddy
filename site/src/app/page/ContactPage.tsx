import { useEffect, useState } from "react";
import "./ContactPage.scss";
import {
  addToFriendlist,
  getCurrentUser,
  updateUser,
} from "../requests/dataRequest";
import { useRecoilState } from "recoil";
import { UserData, userDataState } from "../store/userAtom";
import User from "../types/User";
import { table } from "console";

export default function ContactPage(): JSX.Element {
  const [friendEmail, setFriendEmail] = useState("");
  const [userData, _] = useRecoilState(userDataState);
  const [friendList, setFriendList] = useState<User[]>([]);

  useEffect(() => {
    updateFriendList();
  }, []);

  function updateFriendList(): void {
    if (userData)
      getCurrentUser(userData).then((res) => {
        setFriendList(res.friends);
      });
  }

  function removeFriend(id: number) {
    if (userData)
      getCurrentUser(userData)
        .then(async (res) => {
          res.friends = res.friends.filter((f) => f.id !== id);
          await updateUser(res, userData);
        })
        .then(() => {
          updateFriendList();
        });
  }

  return (
    <div className="contactpage">
      <div className="contactpage__addfriend">
        <input
          type="text"
          value={friendEmail}
          onChange={(e) => setFriendEmail(e.target.value)}
        />
        <button
          onClick={() => {
            addToFriendlist(userData as UserData, friendEmail).then(() =>
              updateFriendList()
            );
          }}
        >
          Ajouter
        </button>
      </div>
      <div className="contactpage__friendlist">
        {friendList.length === 0 ? (
          <span>No friend yet</span>
        ) : (
          <table className="contactpage__friendlist__table">
            <thead>
              <tr>
                <th>
                  <span>Name</span>
                </th>
                <th>
                  <span>Email</span>
                </th>
                <th>
                  <span>Action</span>
                </th>
              </tr>
            </thead>
            <tbody>
              {friendList.map((friend) => (
                <tr key={friend.id}>
                  <td>
                    <span>{friend.name}</span>
                  </td>
                  <td>
                    <span>{friend.email}</span>
                  </td>
                  <td>
                    <button onClick={() => removeFriend(friend.id)}>
                      Supprimer
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
