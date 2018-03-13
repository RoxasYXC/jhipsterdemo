import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Userinfo } from './userinfo.model';
import { UserinfoPopupService } from './userinfo-popup.service';
import { UserinfoService } from './userinfo.service';

@Component({
    selector: 'jhi-userinfo-dialog',
    templateUrl: './userinfo-dialog.component.html'
})
export class UserinfoDialogComponent implements OnInit {

    userinfo: Userinfo;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private userinfoService: UserinfoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userinfo.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userinfoService.update(this.userinfo));
        } else {
            this.subscribeToSaveResponse(
                this.userinfoService.create(this.userinfo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Userinfo>>) {
        result.subscribe((res: HttpResponse<Userinfo>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Userinfo) {
        this.eventManager.broadcast({ name: 'userinfoListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-userinfo-popup',
    template: ''
})
export class UserinfoPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userinfoPopupService: UserinfoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.userinfoPopupService
                    .open(UserinfoDialogComponent as Component, params['id']);
            } else {
                this.userinfoPopupService
                    .open(UserinfoDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
