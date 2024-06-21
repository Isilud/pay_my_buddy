import { atom } from "recoil";

export interface UserData {
  jwtToken: string;
  id: number;
}

export const userDataState = atom<UserData | null>({
  key: "userDataState",
  default: null,
});
