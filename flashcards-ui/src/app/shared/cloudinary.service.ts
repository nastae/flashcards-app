import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

interface CloudinaryUploadResponse {
  secure_url: string;
  public_id: string;
  width: number;
  height: number;
  format: string;
}

@Injectable({
  providedIn: 'root',
})
export class CloudinaryService {

  private readonly http = inject(HttpClient);

  private readonly cloudName = 'whtxcakj';
  private readonly uploadPreset = 'Flashcards-app';

  private readonly uploadUrl = `https://api.cloudinary.com/v1_1/${this.cloudName}/image/upload`;

  uploadImage(file: File): Observable<string> {
    const formData = new FormData();

    formData.append('file', file);
    formData.append('upload_preset', this.uploadPreset);

    return this.http
      .post<CloudinaryUploadResponse>(this.uploadUrl, formData)
      .pipe(
        map(response => response.secure_url)
      );
  }
}
