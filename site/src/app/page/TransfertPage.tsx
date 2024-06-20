import { useNavigate } from "react-router-dom";
import "./TransfertPage.scss";
import { useState, useEffect } from "react";
import { useRecoilState } from "recoil";
import {
  addTransaction,
  getCurrentUser,
  getTransactions,
} from "../requests/dataRequest";
import { userDataState } from "../store/userAtom";
import User from "../types/User";
import { Transaction, TransactionForm } from "../types/Transaction";

export default function TransfertPage(): JSX.Element {
  const navigate = useNavigate();
  const [userData, _] = useRecoilState(userDataState);
  const [friendList, setFriendList] = useState<User[]>([]);
  const [selectedFriend, setSelectedFriend] = useState<string>("");
  const [amountValue, setAmountValue] = useState<number | null>(0);
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  useEffect(() => {
    if (userData) {
      getCurrentUser(userData).then((res) => {
        setFriendList(res.friends);
      });
      updateTransaction();
    }
  }, [userData]);

  function updateTransaction(): void {
    getTransactions(userData!).then((res) => {
      setTransactions(res);
    });
  }

  function createTransactionWithFriend(friendId: string, amount: number): void {
    const transaction: TransactionForm = {
      amount,
      friendId,
      description: "Description",
      withBank: false,
      userId: userData!.id,
      operation: "DEPOSIT",
    };
    addTransaction(transaction, userData!).then(() => {
      updateTransaction();
      setSelectedFriend("");
      setAmountValue(0);
    });
  }

  return (
    <div className="transfertpage">
      <div className="transfertpage__header">
        <span>Faire un virement</span>{" "}
        <button onClick={() => navigate("/contact")}>Ajouter un ami</button>
      </div>
      <div className="transfertpage__paysection">
        <select
          name="friends"
          onChange={(e) => {
            setSelectedFriend(e.target.value);
          }}
        >
          <option value={selectedFriend}>Choisissez un contact</option>
          {friendList.map((friend) => (
            <option key={friend.id} value={friend.id}>
              {friend.name}
            </option>
          ))}
        </select>
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
            createTransactionWithFriend(selectedFriend, amountValue ?? 0);
          }}
          disabled={!amountValue || amountValue === 0}
        >
          Virement
        </button>
      </div>
      <span>Mes transactions</span>
      <table className="transfertpage__table">
        <thead>
          <th>
            <span>Connexion</span>
          </th>
          <th>
            <span>Description</span>
          </th>
          <th>
            <span>Montant</span>
          </th>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.id}>
              <td>
                <span>
                  {(!transaction.withBank &&
                    transaction.userId === userData?.id) ||
                  transaction.withBank
                    ? transaction.friendName
                    : transaction.userName}
                </span>
              </td>
              <td>
                <span>{transaction.description}</span>
              </td>
              <td>
                <span>
                  {transaction.operation === "DEPOSIT" &&
                    transaction.userId === userData?.id &&
                    `-`}
                  {transaction.amount}€
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
