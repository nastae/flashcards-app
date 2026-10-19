import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FlashcardResponse } from '../models/flashcard-response';
import { FlashcardRequest } from '../models/flashcard-request';

@Injectable({
  providedIn: 'root',
})
export class Flashcard {

  private apiUrl = "http://localhost:8080/api/flashcards"

  constructor(private http: HttpClient) {}

  createFlashcard(groupId: string, request: FlashcardRequest): Observable<FlashcardResponse> {
    return this.http.post<FlashcardResponse>(`${this.apiUrl}/${groupId}`, request);
  }

  retryImageGeneration(groupId: number, flashcardId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/groups/${groupId}/flashcards/${flashcardId}/image-generation/retry`, {});
  }

  getByGroupId(groupId: string): Observable<FlashcardResponse[]> {
    return this.http.get<FlashcardResponse[]>(`${this.apiUrl}/group/${groupId}`);
  }
}
