import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Globals } from '../../global/globals';
import { Fail } from '../../dtos/fails/fail';

@Injectable({
  providedIn: 'root'
})
export class LikesService {

  private likesBaseUrl: string = this.globals.backendUrl + '/likes';

  constructor(
    private globals: Globals,
    private httpClient: HttpClient
  ) { }

  likeFail(failId: number): Observable<Fail> {
    let param = new HttpParams();
    param = param.append('failId', failId);
    return this.httpClient.put<Fail>(this.likesBaseUrl + `/like`, {}, {params: param});
  }

  unlikeFail(failId: number): Observable<Fail> {
    let param = new HttpParams();
    param = param.append('failId', failId);
    return this.httpClient.put<Fail>(this.likesBaseUrl + `/unlike`, {}, {params: param});
  }

  getIsFailLikedByUser(failId: number, currUserId: number): Observable<boolean> {
    let params = new HttpParams();
    params = params.append('failId', failId);
    params = params.append('currUserId', currUserId);
    return this.httpClient.get<boolean>(this.likesBaseUrl, { params: params });
  }

}
