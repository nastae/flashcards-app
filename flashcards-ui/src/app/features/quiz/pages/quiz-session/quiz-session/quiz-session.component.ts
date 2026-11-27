import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatAnchor } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { QuizResultResponse } from '../../../models/quiz-result-response.interface';
import { QuizQuestionResponse } from '../../../models/quiz-question-response.interface';
import { QuizAnswerResponse } from '../../../models/quiz-answer-response.interface';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { QuizOptionResponse } from '../../../models/quiz-option-response.interface';
import { QuizService } from '../../../services/quiz.service';
import { QuizAnswerRequest } from '../../../models/quiz-answer-request.interface';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { switchMap, throwError } from 'rxjs';

@Component({
  selector: 'app-quiz-session',
  imports: [MatCardModule, MatAnchor, MatProgressBarModule, MatIconModule],
  templateUrl: './quiz-session.component.html',
  styleUrl: './quiz-session.component.scss',
})
export class QuizSessionComponent implements OnInit {

  readonly router = inject(Router);
  readonly route = inject(ActivatedRoute);
  readonly quizService = inject(QuizService);

  readonly sessionId = signal<number | null>(null);
  readonly currentQuestion = signal<QuizQuestionResponse | null>(null);

  readonly selectedOptionId = signal<number | null>(null);
  readonly answerResult = signal<QuizAnswerResponse | null>(null);
  readonly result = signal<QuizResultResponse | null>(null);

  readonly loading = signal(true);// todo: set true
  readonly submitting = signal(false);
  readonly movingNext = signal(false);
  readonly errorMessage = signal('');
  readonly showResult = signal(false);

  readonly answered = computed(() =>
    this.answerResult() !== null
  );

  readonly score = signal(0);

  readonly progress = computed(() => {
    const question = this.currentQuestion();
    const result = this.result();

    if (result) {
      return 100;
    }

    return question
      ? ((question.number - 1) / this.totalQuestions()) * 100
      : 0;
  });

  private totalQuestionCount = signal(0);

  readonly totalQuestions = this.totalQuestionCount.asReadonly();

  groupId!: number;

  ngOnInit(): void {
    this.groupId = Number(this.route.snapshot.paramMap.get('id'));

    if (!Number.isInteger(this.groupId) || this.groupId <= 0) {
      this.errorMessage.set('Invalid group.');
      this.loading.set(false);
      return;
    }

    const sessionIdParam = this.route.snapshot.paramMap.get('sessionId');

    if (sessionIdParam !== null) {
      const sessionId = Number(sessionIdParam);

      if (!Number.isInteger(sessionId) || sessionId <= 0) {
        this.errorMessage.set('Invalid session id.');
        this.loading.set(false);
        return;
      }

      this.sessionId.set(sessionId ? sessionId : null);
      this.loadCurrentQuestion();
    }
  }

  loadCurrentQuestion(): void {
    const sessionId = this.sessionId();

    if (!sessionId) {
      return;
    }

    this.loading.set(true);

    this.quizService.getCurrentQuestion(sessionId).subscribe({
      next: (question) => {
        this.currentQuestion.set(question);
        this.selectedOptionId.set(null);
        this.answerResult.set(null);
        this.totalQuestionCount.set(question.totalQuestions);
        this.score.set(question.score);
        this.loading.set(false);
      },
      error: (error) => {
        this.handleError(
          error,
          'Failed to load the current question.'
        )
        this.loading.set(false);
      }
    });
  }

  startQuiz(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.quizService.startQuiz(this.groupId).subscribe({
      next: response => {
        this.sessionId.set(response.sessionId);
        this.totalQuestionCount.set(response.totalQuestions);
        this.currentQuestion.set(response.question);
        this.loading.set(false);
        this.score.set(0);

        this.router.navigate(['/groups', this.groupId, 'sessions', response.sessionId, 'quiz']);
      },
      error: error => {
        this.handleError(
          error,
          "Could not start the quiz. Please try again"
        );
      }
    });
  }

  selectAnswer(option: QuizOptionResponse): void {
    const sessionId = this.sessionId();
    const question = this.currentQuestion();

    if (!sessionId || !question || this.answered() || this.submitting()) {
      return;
    }

    this.selectedOptionId.set(option.flashcardId);
    this.submitting.set(true);

    this.quizService.submitAnswer(sessionId, {
      questionId: question.questionId,
      selectedFlashcardId: option.flashcardId
    } as QuizAnswerRequest).subscribe({
      next: response => {
        this.answerResult.set(response);
        this.submitting.set(false);
        this.score.set(response.correctAnswers);
      },
      error: error => {
        this.selectedOptionId.set(null);
        this.submitting.set(false);

        this.handleError(
          error,
          'Could not submit your answer. Please try again.'
        );
      }
    });
  }

  showQuizResult(): void {
    if (!this.answerResult()?.quizCompleted) {
      return;
    }

    this.showResult.set(true);
    this.loadResult();
  }

  isOptionCorrect(option: QuizOptionResponse): boolean {
    return this.answered() &&
      option.flashcardId === this.answerResult()?.correctFlashcardId;
  }

  isOptionIncorrect(option: QuizOptionResponse): boolean {
    return this.answered() &&
      option.flashcardId === this.selectedOptionId() &&
      !this.isOptionCorrect(option);
  }

  nextQuestion(): void {
    const sessionId = this.sessionId();

    if (!sessionId || !this.answered() || this.movingNext()) {
      return;
    }

    if (this.answerResult()?.quizCompleted) {
      return;
    }

    this.movingNext.set(true);

    this.quizService.nextQuestion(sessionId).subscribe({
      next: question => {
        this.currentQuestion.set(question);
        this.selectedOptionId.set(null);
        this.answerResult.set(null);
        this.movingNext.set(false);
      },
      error: error => {
        this.movingNext.set(false);

        this.handleError(
          error,
          'Could not load next question.'
        );
      }
    });
  }

  private loadResult(): void {
    const sessionId = this.sessionId();

    if (!sessionId) {
      return;
    }

    this.quizService.getResult(sessionId).subscribe({
      next: result => this.result.set(result),
      error: error => {
        this.handleError(
          error,
          'Could not load the quiz result.'
        );
      }
    });
  }

  private handleError(error: any, fallbackMessage: string): void {
    console.error(error);

    this.errorMessage.set(
      error.status === 400 || error.status === 409
        ? error.error?.detail ?? error.error?.message ?? fallbackMessage
        : fallbackMessage
    );

    this.loading.set(false);
  }

  restartQuiz(): void {
    this.sessionId.set(null);
    this.currentQuestion.set(null);
    this.selectedOptionId.set(null);
    this.answerResult.set(null);
    this.result.set(null);
    this.totalQuestionCount.set(0);
    this.showResult.set(false);
    this.score.set(0);

    this.startQuiz();
  }

  finishQuiz(): void {
    this.router.navigate(['/study']);
  }

  backToStudy(): void {
    this.router.navigate(['/study']);
  }
}
