import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { GroupCreate } from "./features/groups/pages/group-create/group-create";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, GroupCreate],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('flashcards-ui');
}
