import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FlashcardResponse } from '../../../flashcards/models/flashcard-response';
import { ActivatedRoute, Router } from '@angular/router';
import { Study } from '../../services/study';
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-study-session',
  imports: [MatCardModule, MatButtonModule],
  templateUrl: './study-session.html',
  styleUrl: './study-session.scss',
})
export class StudySession implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private studyService = inject(Study);

  cards = signal<FlashcardResponse[]>([]);
  currentIndex = signal(0);

  currentCard = computed(() => 
    this.cards()[this.currentIndex()]
  );

  groupId!: string;

  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get("id")!;
    this.loadFlashcards();
  }

  // TODO: make proper error handling
  loadFlashcards(): void {
    this.studyService.getSession(this.groupId).subscribe({
      next: (result) => this.cards.set(result),
      error: (err) => console.log(err)
    });
  }

  previous(): void {
    if (this.currentIndex() > 0) {
      this.currentIndex.update(i => i - 1);
    }
  }

  repeatLater(): void {
    const current = this.currentCard();

    if (!current) {
      return;
    }

    // TODO: update back-end that there is card to learn later, on quick learn add that card or somewhere else
    // this.cards.update(cards => [
    //   ...cards,
    //   current
    // ]);

    this.next();
  }

  next() {
    if (this.currentIndex() < this.cards().length - 1) {
      this.currentIndex.update(i => i + 1);
      return;
    }

    this.finish();
  }

  finish(): void {
    this.router.navigate(['/study']);
  }

  onCreateFlashcard() {
    this.router.navigate(['/groups', this.groupId, 'flashcards', 'create']);
  }
}
