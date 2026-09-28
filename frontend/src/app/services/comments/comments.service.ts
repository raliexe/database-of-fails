import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Globals } from '../../global/globals';
import { Comment } from '../../dtos/comments/comment';
import { CommentCreate } from '../../dtos/comments/comment-create';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private commentsBaseUrl: string = this.globals.backendUrl + '/comments';

  constructor(
    private globals: Globals,
    private httpClient: HttpClient
  ) { }

  createComment(comment: CommentCreate): Observable<Comment> {
    return this.httpClient.post<Comment>(this.commentsBaseUrl, comment);
  }

  getCommentsById(id: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(this.commentsBaseUrl + '/' + id);
  }

}
