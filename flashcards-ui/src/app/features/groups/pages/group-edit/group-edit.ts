import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, NgForm, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInput } from "@angular/material/input";
import { MatAnchor } from "@angular/material/button";
import { ActivatedRoute, Router } from '@angular/router';
import { Group } from '../../services/group';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GroupRequest } from '../../models/group-request';

@Component({
  selector: 'app-group-edit',
  imports: [MatCardModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInput, MatAnchor],
  templateUrl: './group-edit.html',
  styleUrl: './group-edit.scss',
})
export class GroupEdit implements OnInit {
  private fb = inject(FormBuilder);
  private groupService = inject(Group);
  private snackbar = inject(MatSnackBar);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  groupId!: string;

  form = this.fb.nonNullable.group({
    name: ['', Validators.required]
  });

  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get("id")!;

    this.loadGroup();
  }

  // TODO: do proper error handling
  loadGroup(): void {
    this.groupService.getById(this.groupId).subscribe({
      next: (result) => {
        console.log("result: ",  result);
        this.form.patchValue({
          name: result.name
        });
      },
      error: (err) => {
        console.log(err);

        this.snackbar.open('Faile to load group ❌', 'Close', {
          duration: 2000,
          verticalPosition: 'bottom',
          panelClass: ['mobile-snackbar']
        });
      }
    });
  }

  // TODO: do proper error handling
  submit() {
    if (this.form.invalid) {
      return;
    }

    const request: GroupRequest = this.form.getRawValue();

    this.groupService.update(this.groupId, request).subscribe({
      next: () => {
        this.snackbar.open('Group updated ✅', 'Close', {
          duration: 2000,
          verticalPosition: 'bottom',
          panelClass: ['mobile-snackbar']
        });

        setTimeout(() => {
          this.router.navigate(['/groups', this.groupId]);
        }, 500);
      },
      error: (err) => {
        console.log(err);
        this.snackbar.open('Error updating group ❌', 'Close', {
          duration: 2000,
          verticalPosition: 'bottom',
          panelClass: ['mobile-snackbar']
        });
      }
    });
  }
}
