import Account from "./Account";

export default interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  name: string;
  friends: User[];
  account: Account;
}
