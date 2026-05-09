import { Component, inject, OnInit, signal } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { GroupResponse } from '../../models/group-response';
import { MatAnchor, MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { ActivatedRoute, Router } from '@angular/router';
import { Group } from '../../services/group';
import { FlashcardResponse } from '../../../flashcards/models/flashcard-response';
import { MatCardModule } from "@angular/material/card";
import { Flashcard } from '../../../flashcards/services/flashcard';
import { MatMenuModule } from '@angular/material/menu';

@Component({
  selector: 'app-group-view',
  imports: [MatToolbarModule, MatAnchor, MatIconModule, MatCardModule, MatMenuModule, MatButtonModule],
  templateUrl: './group-view.html',
  styleUrl: './group-view.scss',
})
export class GroupView implements OnInit {
  groupId!: string;
  // group?: GroupResponse;
  group = signal<GroupResponse | null>(null);
  loading = signal(true);
  flashcards = signal<FlashcardResponse[]>([]);

  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private groupService = inject(Group);
  private flashcardService = inject(Flashcard)

  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get('id')!;
    this.loadGroup();
    this.loadFlashcards();
  }

  // TODO: somehow handle errors
  loadGroup(): void {
    this.groupService.getById(this.groupId).subscribe({
      next: (result) => this.group.set(result),
      error: (err) => console.log(err)
    });
  }

  // TODO: add proper error handling
  loadFlashcards(): void {
    this.flashcardService.getByGroupId(this.groupId).subscribe({
      next: (data) => {
        this.flashcards.set(data);
          // data.map((item: any) => ({
          //   frontText: 'Test'
          // }))
          // data
        // );
        this.loading.set(false);
      },
      error: (err) => {
        console.log(err);
        this.loading.set(false);
      }
    });
  }

  onCreateFlashcard() {
    this.router.navigate(['/groups', this.groupId, 'flashcards', 'create']);
  }

  onEditGroup() {
    this.router.navigate(['/groups', this.groupId, 'edit']);
  }

  onDeleteGroup() {
    if (confirm("Delete this group?")) {
      this.groupService.delete(this.groupId).subscribe(() => {
        this.router.navigate(['/groups']);
      });
    }
  }

  openFlashcard(id: number) {
    this.router.navigate(['/flashcards', id]);
  }

  startStudy() {
    throw new Error('Method not implemented.');
  }
}
