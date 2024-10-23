export interface User {
  id: number;
  account: string;
  name: string;
  age: number;
  email: string;
  phone: string;
  role: {
    id: number;
    name: string;
  };
  ban: boolean;
}

export interface Register {
  username: string;
  password: string;
}
