import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FlashcardResponse } from '../../flashcards/models/flashcard-response';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class Study {

  private apiUrl = "http://localhost:8080/api/study";

  private http: HttpClient = inject(HttpClient);

  getSession(groupId: string): Observable<FlashcardResponse[]> {
    return this.http.get<FlashcardResponse[]>(`${this.apiUrl}/groups/${groupId}`);
  }
}
