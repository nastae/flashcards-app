import { Routes } from '@angular/router';
import { GroupCreate } from './features/groups/pages/group-create/group-create';
import { GroupList } from './features/groups/pages/group-list/group-list';

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
        path: '',
        redirectTo: 'groups',
        pathMatch: 'full'
    }
];
