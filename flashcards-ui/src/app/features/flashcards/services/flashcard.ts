import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FlashcardResponse } from '../models/flashcard-response';

@Injectable({
  providedIn: 'root',
})
export class Flashcard {

  private apiUrl = "http://localhost:8080/api/flashcards"

  constructor(private http: HttpClient) {}

  getByGroupId(groupId: string): Observable<FlashcardResponse[]> {
    return this.http.get<FlashcardResponse[]>(`${this.apiUrl}/group/${groupId}`);
  }
}
