import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TestSharedModule } from '../../shared';
import {
    UserinfoService,
    UserinfoPopupService,
    UserinfoComponent,
    UserinfoDetailComponent,
    UserinfoDialogComponent,
    UserinfoPopupComponent,
    UserinfoDeletePopupComponent,
    UserinfoDeleteDialogComponent,
    userinfoRoute,
    userinfoPopupRoute,
} from './';

const ENTITY_STATES = [
    ...userinfoRoute,
    ...userinfoPopupRoute,
];

@NgModule({
    imports: [
        TestSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        UserinfoComponent,
        UserinfoDetailComponent,
        UserinfoDialogComponent,
        UserinfoDeleteDialogComponent,
        UserinfoPopupComponent,
        UserinfoDeletePopupComponent,
    ],
    entryComponents: [
        UserinfoComponent,
        UserinfoDialogComponent,
        UserinfoPopupComponent,
        UserinfoDeleteDialogComponent,
        UserinfoDeletePopupComponent,
    ],
    providers: [
        UserinfoService,
        UserinfoPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TestUserinfoModule {}
