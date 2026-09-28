import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Globals } from '../../global/globals';
import { GeneralSearchResponseDto } from '../../dtos/common/GeneralSearchResponseDto';

@Injectable({
  providedIn: 'root'
})
export class GeneralSearchService {

  private generalSearchBaseUrl: string = this.globals.backendUrl + '/search';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
  ) { }

  getAllMatching(term: string): Observable<GeneralSearchResponseDto[]>{
    return this.httpClient.get<GeneralSearchResponseDto[]>(`${this.generalSearchBaseUrl}?term=${term}`);
  }

}
