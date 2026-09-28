import { Image } from '../image/image';

export class User {
  constructor(
    public id: number,
    public nickname: string,
    public email: string,
    public isLocked: boolean,
    public bonusPoints?: number,
    public image?: Image,
    public token?: string
  ) {}
}
