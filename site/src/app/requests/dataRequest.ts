import { UserData } from "../store/userAtom";
import { Transaction, TransactionForm } from "../types/Transaction";
import User from "../types/User";
import { fetchMethod } from "./fetchMethod";

export async function addToFriendlist(userData:UserData, friendEmail : string): Promise<void> {
    return await fetchMethod<void>('PUT', `/user/${friendEmail}/addfriend`, {id : userData.id} satisfies Partial<User>, userData.jwtToken);
}

export async function getCurrentUser(userData:UserData): Promise<User> {
    return await fetchMethod<User>('GET', `/user/${userData.id}/info`, {}, userData.jwtToken);
}

export async function updateUser(user: Partial<User>, userData:UserData): Promise<User> {
    return await fetchMethod<User>('PUT', "/user", user, userData.jwtToken);
}

export async function addTransaction(transaction: TransactionForm, userData:UserData): Promise<TransactionForm> {
    return await fetchMethod<TransactionForm>('POST', "/transaction", transaction, userData.jwtToken);
}

export async function getTransactions(userData:UserData): Promise<Transaction[]> {
    return await fetchMethod<Transaction[]>('GET', `/transaction/${userData.id}`, {}, userData.jwtToken);
}

export async function deleteUserAccount(userData:UserData): Promise<void> {
    return await fetchMethod<void>('DELETE', `/user/${userData.id}/delete`, {}, userData.jwtToken);
}
