import { Routes } from '@angular/router';
import { GroupCreate } from './features/groups/pages/group-create/group-create';
import { GroupList } from './features/groups/pages/group-list/group-list';
import { GroupView } from './features/groups/pages/group-view/group-view';

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
        path: '',
        redirectTo: 'groups',
        pathMatch: 'full'
    }
];
