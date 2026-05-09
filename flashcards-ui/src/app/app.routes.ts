import { Routes } from '@angular/router';
import { GroupCreate } from './features/groups/pages/group-create/group-create';
import { GroupList } from './features/groups/pages/group-list/group-list';
import { GroupView } from './features/groups/pages/group-view/group-view';
import { GroupEdit } from './features/groups/pages/group-edit/group-edit';
import { FlashcardCreate } from './features/flashcards/pages/flashcard-create/flashcard-create';

export const routes: Routes = [
    {
        path: 'groups',
        component: GroupList
    },
    {
        path: 'groups/create',
        component: GroupCreate
    },
    {
        path: 'groups/:id',
        component: GroupView
    },
    {
        path: 'groups/:id/edit',
        component: GroupEdit
    },
    {
        path: '',
        redirectTo: 'groups',
        pathMatch: 'full'
    },
    {
        path: 'groups/:id/flashcards/create',
        component: FlashcardCreate
    }
];
