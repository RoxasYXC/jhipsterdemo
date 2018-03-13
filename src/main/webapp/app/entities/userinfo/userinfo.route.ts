import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { UserinfoComponent } from './userinfo.component';
import { UserinfoDetailComponent } from './userinfo-detail.component';
import { UserinfoPopupComponent } from './userinfo-dialog.component';
import { UserinfoDeletePopupComponent } from './userinfo-delete-dialog.component';

export const userinfoRoute: Routes = [
    {
        path: 'userinfo',
        component: UserinfoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testApp.userinfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'userinfo/:id',
        component: UserinfoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testApp.userinfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userinfoPopupRoute: Routes = [
    {
        path: 'userinfo-new',
        component: UserinfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testApp.userinfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'userinfo/:id/edit',
        component: UserinfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testApp.userinfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'userinfo/:id/delete',
        component: UserinfoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'testApp.userinfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
