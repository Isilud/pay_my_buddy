import { UserData } from "../store/userAtom";
import { fetchMethod } from "./fetchMethod";

  export interface FormAuthentication {
    email: string;
    password: string;
  }

  export interface FormRegistration {
    email: string,
    firstName:string,
    lastName: string,
    password:string,
    account: {
        code :string
    }
}

export async function userAuthentication(formAuthentication : FormAuthentication): Promise<UserData> {
    return await fetchMethod<UserData>('POST', '/authenticate', formAuthentication);
}

export async function userRegistration(formRegistration : FormRegistration): Promise<void> {
    return await fetchMethod('POST', '/register', formRegistration);
}