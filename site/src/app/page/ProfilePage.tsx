import { useRecoilState } from "recoil";
import { userDataState } from "../store/userAtom";
import { useEffect, useState } from "react";
import {
  addTransaction,
  deleteUserAccount,
  getCurrentUser,
} from "../requests/dataRequest";
import User from "../types/User";
import "./ProfilePage.scss";
import { TransactionForm } from "../types/Transaction";
import { deleteLocalUserData } from "../requests/localStorage";

export default function ProfilePage(): JSX.Element {
  const [userData, setUserData] = useRecoilState(userDataState);
  const [userInfo, setUserInfo] = useState<User | null>(null);
  const [amountValue, setAmountValue] = useState<number | null>(0);

  useEffect(() => updateUserInfo(), []);

  function updateUserInfo(): void {
    if (userData)
      getCurrentUser(userData).then((res) => {
        setUserInfo(res);
      });
  }

  function createTransactionWithBank(
    amount: number,
    operation: "DEPOSIT" | "WITHDRAW"
  ): void {
    const transaction: TransactionForm = {
      amount,
      description: "Description",
      withBank: true,
      userId: userData!.id,
      operation,
    };
    addTransaction(transaction, userData!).then(() => {
      updateUserInfo();
      setAmountValue(0);
    });
  }

  if (!userInfo) return <div className="profilepage">Chargement</div>;

  return (
    <div className="profilepage">
      <span className="profilepage__line">
        <h3>Nom :</h3> <span>{userInfo.lastName}</span>
      </span>
      <span className="profilepage__line">
        <h3>Prénom :</h3>
        <span>{userInfo.firstName}</span>
      </span>
      <span className="profilepage__line">
        <h3>Email :</h3>
        <span>{userInfo.email}</span>
      </span>
      <span className="profilepage__line">
        <h3>Numéro de compte :</h3>
        <span>{userInfo.account.code}</span>
      </span>
      <span className="profilepage__line">
        <h3>Solde :</h3>
        <span>{userInfo.account.amount}</span>
      </span>
      <div className="profilepage__accountAction">
        <input
          type="number"
          value={amountValue ?? ""}
          onChange={(e) => {
            const value = !Number.isNaN(e.target.valueAsNumber)
              ? e.target.valueAsNumber
              : null;
            setAmountValue(value);
          }}
          min={0}
          className="profilepage__accountAction__moneyinput"
        />
        <button
          onClick={() => {
            createTransactionWithBank(amountValue ?? 0, "WITHDRAW");
          }}
          disabled={!amountValue || amountValue === 0}
        >
          Retrait
        </button>
        <button
          onClick={() => {
            createTransactionWithBank(amountValue ?? 0, "DEPOSIT");
          }}
          disabled={!amountValue || amountValue === 0}
        >
          Dépot
        </button>
      </div>
      {/*       <button
        onClick={() => {
          deleteUserAccount(userData!).then(() => {
            () => {
              deleteLocalUserData();
              setUserData(null);
            };
          });
        }}
      >
        Supprimer le compte
      </button> */}
    </div>
  );
}
