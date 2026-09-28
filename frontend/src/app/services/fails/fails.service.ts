import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Globals } from '../../global/globals';
import { FailCreate } from '../../dtos/fails/fail-create';
import { Fail } from '../../dtos/fails/fail';

@Injectable({
  providedIn: 'root'
})
export class FailsService {

  private failsBaseUrl: string = this.globals.backendUrl + '/fails';

  constructor(
    private globals: Globals,
    private httpClient: HttpClient
  ) { }

  createFail(failCreateDto: FailCreate, image: File): Observable<FailCreate> {
    const formData = new FormData();
      formData.append('name', failCreateDto.name);
      formData.append('description', failCreateDto.description);
      formData.append('date', failCreateDto.date);
      formData.append('image', image);
    console.log('Create fail with name: ' + failCreateDto.name);
    return this.httpClient.post<FailCreate>(this.failsBaseUrl, formData);
  }

  updateFail(fail: FailCreate, image: File,
             childImages: Array<{ id: number, name: string, file: File, content: string, status: string }>,
             id: string): Observable<Fail> {
    const formData = new FormData();
      formData.append('id', id);
      formData.append('name', fail.name);
      formData.append('description', fail.description);
      formData.append('date', fail.date);
      formData.append('image', image);
      for (var i = 0; i < childImages.length; i++) {
        if (childImages[i].id == null && childImages[i].status == 'ACTIVE') {
          formData.append('images', childImages[i].file);
        } else if (childImages[i].id != null && childImages[i].status == 'DELETED') {
          formData.append('deletedImages', '' + childImages[i].id);
        }
      }
    console.log('Update fail with name: ' + fail.name);
    return this.httpClient.put<Fail>(this.failsBaseUrl, formData);
  }

  getFailsById(id: number): Observable<Fail> {
    console.log('Load fail details for ' + id);
    return this.httpClient.get<Fail>(this.failsBaseUrl + '/' + id);
  }

  getFailsByCurrentUser(): Observable<Fail[]> {
    return this.httpClient.get<Fail[]>(this.failsBaseUrl + '/getAllByCurrentUser');
  }

  getAll(): Observable<Fail[]> {
    console.log('Get all fails');
    return this.httpClient.get<Fail[]>(this.failsBaseUrl + '/getAll');
  }

  deleteFailsById(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.failsBaseUrl + '/' + id);
  }

  getFailPDF(failId: number): Observable<Blob> {
    return this.httpClient.get(this.failsBaseUrl + `/${failId}/invoice`, {responseType: 'blob'});
  }

}
