export class CommentCreate {
  message: string;
  parentId: number;
  failId: number;

  constructor(message: string, parentId: number, failId: number) {
    this.message = message;
    this.parentId = parentId;
    this.failId = failId;
  }

}
