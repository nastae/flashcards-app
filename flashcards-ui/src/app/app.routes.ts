import { Routes } from '@angular/router';
import { GroupCreate } from './features/groups/pages/group-create/group-create';
import { GroupList } from './features/groups/pages/group-list/group-list';
import { GroupView } from './features/groups/pages/group-view/group-view';
import { GroupEdit } from './features/groups/pages/group-edit/group-edit';

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
    }
];
