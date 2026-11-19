import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FlashcardResponse } from '../../../flashcards/models/flashcard-response';
import { ActivatedRoute, Router } from '@angular/router';
import { Study } from '../../services/study';
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from '@angular/material/button';
import { Flashcard } from '../../../flashcards/services/flashcard';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { StudyPreferencesService } from '../../services/study-preferences/study-preferences.service';
import { MatButtonToggleGroup, MatButtonToggle } from '@angular/material/button-toggle';
import { MatExpansionModule } from '@angular/material/expansion';
import { StudyDirection } from '../../models/study-direction/study-direction.type';
import { GroupResponse } from '../../../groups/models/group-response';
import { LanguageResponse } from '../../../groups/models/language-response';
import { Group } from '../../../groups/services/group';
import { LanguageService } from '../../../groups/services/language.service';

@Component({
  selector: 'app-study-session',
  imports: [
    MatCardModule, 
    MatButtonModule, 
    MatSlideToggleModule, 
    MatButtonToggleGroup, 
    MatButtonToggle,
    MatExpansionModule
  ],
  templateUrl: './study-session.html',
  styleUrl: './study-session.scss',
})
export class StudySession implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private studyService = inject(Study);
  private readonly flashcardService = inject(Flashcard);
  private readonly studyPreferences = inject(StudyPreferencesService);
  private readonly groupService = inject(Group);
  private readonly languageService = inject(LanguageService);

  readonly group = signal<GroupResponse | null>(null);
  readonly languages = signal<LanguageResponse[]>([]);
  cards = signal<FlashcardResponse[]>([]);
  currentIndex = signal(0);

  readonly studyDirection = signal<StudyDirection>(
    this.studyPreferences.studyDirection()
  );

  readonly alwaysShowAnswers = signal(
    this.studyPreferences.alwaysShowAnswers()
  );

  readonly answerRevealed = signal(false);

  readonly answerVisible = computed(
    () => this.alwaysShowAnswers() || this.answerRevealed()
  );

  readonly nativeLanguageLabel = computed(() => {
    const code = this.group()?.nativeLanguageCode;

    return this.languages().find(language => language.code === code)
      ?.nativeName ?? 'Native language';
  });

  readonly learningLanguageLabel = computed(() => {
    const code = this.group()?.learningLanguageCode;

    return this.languages().find(language => language.code === code)
      ?.nativeName ?? 'Learning language';
  });

  currentCard = computed(() => 
    this.cards()[this.currentIndex()]
  );

  readonly showQuestionWord = computed(() => {
    const card = this.currentCard();

    if (!card) {
      return false;
    }

    return this.studyDirection() === 'NATIVE_TO_LEARNING'
      ? !!card.nativeWord
      : !!card.frontText;
  });

  readonly showAnswerWord = computed(() => {
    const card = this.currentCard();

    if (!card) {
      return false;
    }

    return this.studyDirection() === 'NATIVE_TO_LEARNING'
      ? !!card.frontText
      : !!card.nativeWord;
  });

  readonly questionLanguageLabel = computed(() =>
    this.studyDirection() === 'NATIVE_TO_LEARNING'
      ? this.nativeLanguageLabel()
      : this.learningLanguageLabel()
  );

  readonly answerLanguageLabel = computed(() =>
    this.studyDirection() === 'NATIVE_TO_LEARNING'
      ? this.learningLanguageLabel()
      : this.nativeLanguageLabel()
  );

  readonly questionWord = computed(() => {
    const card = this.currentCard();

    if (!card) {
      return '';
    }

    return this.studyDirection() === 'NATIVE_TO_LEARNING'
      ? card.nativeWord
      : card.frontText;
  });

  readonly answerWord = computed(() => {
    const card = this.currentCard();

    if (!card) {
      return '';
    }

    return this.studyDirection() === 'NATIVE_TO_LEARNING'
      ? card.frontText
      : card.nativeWord;
  });

  groupId!: string;

  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get("id")!;
    this.loadGroup();
    this.loadLanguages();
    this.loadFlashcards();
  }

  // TODO: make proper error handling
  loadFlashcards(): void {
    this.studyService.getSession(this.groupId).subscribe({
      next: (result) => this.cards.set(result),
      error: (err) => console.log(err)
    });
  }

  private loadGroup(): void {
    this.groupService.getById(this.groupId).subscribe({
      next: group => this.group.set(group),
      error: error => console.error('Failed to load group: ', error)
    });
  }

  private loadLanguages(): void {
    this.languageService.getLanguages().subscribe({
      next: languages => this.languages.set(languages),
      error: error => console.error('Failed to load languages: ', error)
    });
  }

  onStudyDirectionChange(direction: StudyDirection): void {
    this.studyDirection.set(direction);
    this.studyPreferences.setStudyDirection(direction);
    this.answerRevealed.set(false);
  }

  onAlwaysShowAnswersChange(value: boolean): void {
    this.alwaysShowAnswers.set(value);
    this.studyPreferences.setAlwaysShowAnswers(value);
  }

  toggleAnswer(): void {
    this.answerRevealed.update(revealed => !revealed);
  }

  previous(): void {
    if (this.currentIndex() > 0) {
      this.currentIndex.update(i => i - 1);
      this.answerRevealed.set(false);
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
      this.answerRevealed.set(false);
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

  retryImageGeneration(flashcardId: number): void {
    const numericGroupId = Number(this.groupId);

    if (isNaN(numericGroupId)) {
      console.error("Invalid group ID:", this.groupId);
      return;
    }

    this.flashcardService.retryImageGeneration(numericGroupId, flashcardId).subscribe({
      next: () => {
        console.log("Retry initiated successfully!")
      },
      error: (err) => {
        console.error("Failed to retry image generatino", err);
      }
    });
  }
}
