import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Group } from '../../services/group';
import { GroupRequest } from '../../models/group-request';

@Component({
  selector: 'app-group-create',
  imports: [ReactiveFormsModule],
  templateUrl: './group-create.html',
  styleUrl: './group-create.scss',
})
export class GroupCreate {

  // TODO: add creation logic and html

  private fb = inject(FormBuilder);
  private groupService = inject(Group);

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
        this.successMessage = 'Group created successfully!';
        this.errorMessage = '';
        this.form.reset();
      },
      error: () => {
        this.errorMessage = 'Failed to create group';
        this.successMessage = '';
      }
    })
  }
}
