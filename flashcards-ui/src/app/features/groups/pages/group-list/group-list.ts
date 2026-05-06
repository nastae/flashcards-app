import { Component, inject, OnInit, signal } from '@angular/core';
import { Group } from '../../services/group';
import { GroupResponse } from '../../models/group-response';
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-group-list',
  imports: [MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './group-list.html',
  styleUrl: './group-list.scss',
})
export class GroupList implements OnInit {
  protected groups = signal<GroupResponse[]>([]);
  protected loading = signal(true);

  private groupService = inject(Group);
  private router = inject(Router);

  ngOnInit(): void {
    // throw new Error('Method not implemented.');
    this.loadGroups();
  }

  loadGroups(): void {
    this.groupService.getAll().subscribe({
      next: (data) => {
        this.groups.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.log(err);
        this.loading.set(false);
      } 
    });
  }

  openGroup(groupId: number) {
    // throw new Error('Method not implemented.');
    this.router.navigate(['/groups', groupId]);
  }

  goToCreate() {
    // throw new Error('Method not implemented.');
    this.router.navigate(['/groups/create']);
  }
}
