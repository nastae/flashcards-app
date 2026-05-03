import { Routes } from '@angular/router';
import { GroupCreate } from './features/groups/pages/group-create/group-create';

export const routes: Routes = [
    {
        path: 'groups/create',
        component: GroupCreate
    }
];
