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
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { CloudinaryService } from '../../../../shared/cloudinary.service';

@Component({
  selector: 'app-flashcard-create',
  imports: [MatCardModule, ReactiveFormsModule, MatFormFieldModule, MatInput, MatAnchor, MatButtonToggleModule],
  templateUrl: './flashcard-create.html',
  styleUrl: './flashcard-create.scss',
})
export class FlashcardCreate implements OnInit {
  private fb = inject(FormBuilder);
  private flashcardService = inject(Flashcard);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cloudinaryService = inject(CloudinaryService);

  private groupId!: string;

  form = this.fb.nonNullable.group({
    frontText: ['', Validators.required],
    backText: ['', Validators.required],
    imageUrl: ['']
  });

  imageSource: 'url' | 'upload' = 'url';

  selectedFile: File | null = null;

  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get("id")!;
  }

  // TODO: make proper error handling
  submit(formDirective: any) {
    if (this.form.invalid) {
      // this.form.markAllAsTouched();
      return;
    }

    if (this.imageSource === 'upload' && !this.selectedFile) {
      this.snackBar.open('Please select an image', 'Close', {
        duration: 2000,
        verticalPosition: 'bottom',
        panelClass: ['mobile-snackbar']
      });

      return;
    }

    if (this.imageSource === 'upload') {
      this.uploadAndCreateFlashcard(formDirective);
      return;
    }

    this.createFlashcard(formDirective, this.form.getRawValue().imageUrl);

    // const request: FlashcardRequest = this.form.getRawValue();

    // this.flashcardService.createFlashcard(this.groupId, request).subscribe({
    //   next: () => {
    //     this.snackBar.open('Flashcard created ✅', 'Close', {
    //       duration: 2000,
    //       verticalPosition: 'bottom',
    //       panelClass: ['mobile-snackbar']
    //     });

    //     // TODO: check if needed this.form.reset() (and check on group creation page ts file also)
    //     this.form.reset();
    //     formDirective.resetForm();

    //     setTimeout(() => {
    //       this.router.navigate(['/groups', this.groupId]);
    //     }, 500);
    //   },
    //   error: (err) => {
    //     console.log(err);

    //     this.snackBar.open('Error creating flashcard ❌', 'Close', {
    //       duration: 2000,
    //       verticalPosition: 'bottom',
    //       panelClass: ['mobile-snackbar']
    //     });
    //   }
    // });
  }

  private uploadAndCreateFlashcard(formDirective: any): void {
    if (!this.selectedFile) {
      return;
    }

    this.cloudinaryService.uploadImage(this.selectedFile).subscribe({
      next: (imageUrl) => {
        this.createFlashcard(formDirective, imageUrl);
      },
      // TODO: make proper error handling
      error: (err) => {
        console.log(err);

        this.snackBar.open('Error uploading image ❌', 'Close', {
          duration: 2000,
          verticalPosition: 'bottom',
          panelClass: ['mobile-snackbar']
        });
      }
    });
  }

  private createFlashcard(formDirective: any, imageUrl: string): void {

    const request: FlashcardRequest = {
      ...this.form.getRawValue(),
      imageUrl
    }

    this.flashcardService
      .createFlashcard(this.groupId, request)
      .subscribe({
        next: () => {

          this.snackBar.open('Flashcard created ✅', 'Close', {
            duration: 2000,
            verticalPosition: 'bottom',
            panelClass: ['mobile-snackbar']
          });

          this.form.reset();
          formDirective.resetForm();

          this.selectedFile = null;
          this.imageSource = 'url';

          setTimeout(() => {
            this.router.navigate(['/groups', this.groupId]);
          }, 500);
        },
        // TODO: add proper error handling
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

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      this.selectedFile = null;
      return;
    }

    this.selectedFile = input.files[0];
  }

  onImageSourceChange(source: 'url' | 'upload'): void {
    this.imageSource = source;

    const imageUrlControl = this.form.controls.imageUrl;

    if (source === 'url') {
      imageUrlControl.setValidators([
        // Validators.required
        Validators.pattern(/^https?:\/\/.+/)
      ]);
    } else {
      imageUrlControl.clearValidators();
    }

    imageUrlControl.updateValueAndValidity();
  }
}
