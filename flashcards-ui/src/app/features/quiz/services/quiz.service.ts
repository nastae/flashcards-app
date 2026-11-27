import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { QuizSessionResponse } from '../models/quiz-session-response.interface';
import { QuizAnswerResponse } from '../models/quiz-answer-response.interface';
import { QuizAnswerRequest } from '../models/quiz-answer-request.interface';
import { QuizQuestionResponse } from '../models/quiz-question-response.interface';
import { QuizResultResponse } from '../models/quiz-result-response.interface';

@Injectable({
  providedIn: 'root',
})
export class QuizService {
  private apiUrl = "http://localhost:8080/api/quiz"

  private http = inject(HttpClient);

  getCurrentQuestion(sessionId: number): Observable<QuizQuestionResponse> {
    return this.http.get<QuizQuestionResponse>(
      `${this.apiUrl}/quiz-sessions/${sessionId}/current-question`
    );
  }

  startQuiz(groupId: number): Observable<QuizSessionResponse> {
    return this.http.post<QuizSessionResponse>(
      `${this.apiUrl}/groups/${groupId}/quiz-sessions`,
      {}
    );
  }

  submitAnswer(
    sessionId: number,
    request: QuizAnswerRequest
  ): Observable<QuizAnswerResponse> {
    return this.http.post<QuizAnswerResponse>(
      `${this.apiUrl}/quiz-sessions/${sessionId}/answers`,
      request
    );
  }

  nextQuestion(sessionId: number): Observable<QuizQuestionResponse> {
    return this.http.post<QuizQuestionResponse>(
      `${this.apiUrl}/quiz-sessions/${sessionId}/next`,
      {}
    );
  }

  getResult(sessionId: number): Observable<QuizResultResponse> {
    return this.http.get<QuizResultResponse>(
      `${this.apiUrl}/quiz-sessions/${sessionId}/result`
    );
  }
}
