import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LanguageResponse } from '../models/language-response';

@Injectable({
  providedIn: 'root',
})
export class LanguageService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = "http://localhost:8080/api/languages";

  getLanguages(): Observable<LanguageResponse[]> {
    return this.http.get<LanguageResponse[]>(this.apiUrl);
  }
}
