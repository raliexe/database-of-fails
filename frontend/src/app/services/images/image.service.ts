import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Globals } from '../../global/globals';
import { Image } from '../../dtos/image/image';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  private imagesBaseUrl: string = this.globals.backendUrl + '/images';

  constructor(
    private globals: Globals,
    private httpClient: HttpClient
  ) { }

  uploadImage(image: File): Observable<Image> {
    console.log('uploadImage', image);
    if (!image) {
      return new Observable<Image>();
    }
    const data: FormData = new FormData();
    data.append('image', image, image.name);
    return this.httpClient.post<Image>(this.imagesBaseUrl, data);
  }

}
