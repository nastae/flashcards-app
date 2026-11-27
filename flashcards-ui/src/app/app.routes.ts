import { Routes } from '@angular/router';
import { GroupCreate } from './features/groups/pages/group-create/group-create';
import { GroupList } from './features/groups/pages/group-list/group-list';
import { GroupView } from './features/groups/pages/group-view/group-view';
import { GroupEdit } from './features/groups/pages/group-edit/group-edit';
import { FlashcardCreate } from './features/flashcards/pages/flashcard-create/flashcard-create';
import { StudyList } from './features/study/pages/study-list/study-list';
import { StudySession } from './features/study/pages/study-session/study-session';
import { QuizSessionComponent } from './features/quiz/pages/quiz-session/quiz-session/quiz-session.component';

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
        redirectTo: 'study',
        pathMatch: 'full'
    },
    {
        path: 'groups/:id/flashcards/create',
        component: FlashcardCreate
    },
    // TODO: update to /groups/study
    {
        path: 'study',
        component: StudyList
    },
    // TODO: update to /groups/:id/study
    {
        path: 'study/:id',
        component: StudySession
    },
    {
        path: 'groups/:id/sessions/:sessionId/quiz',
        component: QuizSessionComponent
    }
];
