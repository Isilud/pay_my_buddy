export type TransactionForm =
  | {
      userId: number;
      friendId: string;
      amount: number;
      description: string;
      operation: "DEPOSIT";
      withBank: false;
    }
  | {
      userId: number;
      amount: number;
      description: string;
      operation: "DEPOSIT" | "WITHDRAW";
      withBank: true;
    };

export interface Transaction {
  id: number;
  userId: number;
  friendName: string;
  userName: string;
  amount: number;
  description: string;
  operation: "DEPOSIT" | "WITHDRAW";
  withBank: true;
}
