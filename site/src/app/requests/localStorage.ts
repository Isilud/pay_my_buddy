import { UserData } from "../store/userAtom";

const STORAGE_KEY = "userData"

export function setLocalUserData(userData : UserData) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(userData));
}

export function getLocalUserData() : UserData | null {
    const storedUserData = localStorage.getItem(STORAGE_KEY)
    if (!storedUserData) return null
    const userData = JSON.parse(storedUserData) as UserData
    if(!userData.jwtToken || !userData.id)
        return null;
    return userData;
}

export function deleteLocalUserData() {
    localStorage.removeItem(STORAGE_KEY);
}