import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Group } from '../../services/group';
import { GroupRequest } from '../../models/group-request';
import { MatCardModule } from '@angular/material/card';
import { MatError, MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-group-create',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule
    // MatFormField,
    // MatLabel,
    // MatInput,
    // MatError
  ],
  templateUrl: './group-create.html',
  styleUrl: './group-create.scss',
})
export class GroupCreate {

  // TODO: add creation logic and html

  private fb = inject(FormBuilder);
  private groupService = inject(Group);
  private snackBar = inject(MatSnackBar);

  form = this.fb.nonNullable.group({
    name: ['', Validators.required]
  });

  successMessage = '';
  errorMessage = '';

  constructor() {}

  submit() {
    if (this.form.invalid) {
      return;
    }

    const request: GroupRequest = this.form.getRawValue();
    this.groupService.createGroup(request).subscribe({
      next: (res) => {
        // this.successMessage = 'Group created successfully!';
        // this.errorMessage = '';
        this.snackBar.open('Group created ✅', 'Close', {
          duration: 2000
        });
        this.form.reset();
      },
      error: () => {
        // this.errorMessage = 'Failed to create group';
        // this.successMessage = '';
        this.snackBar.open('Error creating group ❌', 'Close', {
          duration: 2000
        });
      }
    })
  }
}
