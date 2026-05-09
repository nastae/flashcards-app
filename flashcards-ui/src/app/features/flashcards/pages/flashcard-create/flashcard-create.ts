import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { MatAnchor } from "@angular/material/button";
import { ActivatedRoute, Router } from '@angular/router';
import { FlashcardRequest } from '../../models/flashcard-request';
import { Flashcard } from '../../services/flashcard';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-flashcard-create',
  imports: [MatCardModule, ReactiveFormsModule, MatFormFieldModule, MatInput, MatAnchor],
  templateUrl: './flashcard-create.html',
  styleUrl: './flashcard-create.scss',
})
export class FlashcardCreate implements OnInit {
  private fb = inject(FormBuilder);
  private flashcardService = inject(Flashcard);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  private groupId!: string;

  form = this.fb.nonNullable.group({
    frontText: ['', Validators.required],
    backText: ['', Validators.required],
    imageUrl: ['', Validators.required]
  });

  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get("id")!;
  }

  // TODO: make proper error handling
  submit(formDirective: any) {
    if (this.form.invalid) {
      return;
    }

    const request: FlashcardRequest = this.form.getRawValue();

    this.flashcardService.createFlashcard(this.groupId, request).subscribe({
      next: () => {
        this.snackBar.open('Flashcard created ✅', 'Close', {
          duration: 2000,
          verticalPosition: 'bottom',
          panelClass: ['mobile-snackbar']
        });

        // TODO: check if needed this.form.reset() (and check on group creation page ts file also)
        this.form.reset();
        formDirective.resetForm();

        setTimeout(() => {
          this.router.navigate(['/groups', this.groupId]);
        }, 500);
      },
      error: (err) => {
        console.log(err);

        this.snackBar.open('Error creating flashcard ❌', 'Close', {
          duration: 2000,
          verticalPosition: 'bottom',
          panelClass: ['mobile-snackbar']
        });
      }
    });
  }
}
