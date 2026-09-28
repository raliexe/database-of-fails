import { Image } from '../image/image';

export class UserRegister {
  constructor(
    public nickname: string,
    public email: string,
    public password: string,
    public passwordConfirmation: string,
    public admin: boolean
  ) {
  }

}
