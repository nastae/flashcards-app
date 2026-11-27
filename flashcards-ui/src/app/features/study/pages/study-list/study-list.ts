import { Component, inject, OnInit, signal } from '@angular/core';
import { GroupResponse } from '../../../groups/models/group-response';
import { MatCardModule } from "@angular/material/card";
import { CdkOverlayOrigin } from "@angular/cdk/overlay";
import { Group } from '../../../groups/services/group';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { QuizService } from '../../../quiz/services/quiz.service';

@Component({
  selector: 'app-study-list',
  imports: [MatCardModule, MatButtonModule],
  templateUrl: './study-list.html',
  styleUrl: './study-list.scss',
})
export class StudyList implements OnInit {
  protected groups = signal<GroupResponse[]>([]);
  protected loading = signal(true);

  private groupService = inject(Group);
  private router = inject(Router);
  private quizService = inject(QuizService);

  ngOnInit(): void {
    this.loadGroups();
  }

  loadGroups(): void {
    this.groupService.getAll().subscribe({
      next: (result) => {
        this.groups.set(result);
        this.loading.set(false);
      },
      error: (err) => {
        console.log(err);
        this.loading.set(false);
      }
    });
  }

  startStudy(groupId: number): void {
    this.router.navigate(['/study', groupId]);
  }

  startQuiz(groupId: number): void {
    this.quizService.startQuiz(groupId).subscribe({
      next: response => {
        this.router.navigate(['/groups', groupId, 'sessions', response.sessionId, 'quiz']);
      },
      error: error => {
        console.error('Failed to start quiz.', error);
      }
    });
  }

  continueQuiz(groupId: number, sessionId: number): void {
    this.router.navigate(['/groups', groupId, 'sessions', sessionId, 'quiz']);
  }
}
