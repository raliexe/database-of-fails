import { Fail } from '../../dtos/fails/fail';
import { User } from '../../dtos/user/user';

export class Comment {
  id: number;
  message: string;
  comments: Comment[];
  fail: Fail;
  user: User;

  constructor(id: number, message: string, comments: Comment[], fail: Fail, user: User) {
    this.id = id;
    this.message = message;
    this.comments = comments;
    this.fail = fail;
    this.user = user;
  }

}
