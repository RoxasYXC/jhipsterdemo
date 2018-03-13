import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Userinfo } from './userinfo.model';
import { UserinfoService } from './userinfo.service';

@Component({
    selector: 'jhi-userinfo-detail',
    templateUrl: './userinfo-detail.component.html'
})
export class UserinfoDetailComponent implements OnInit, OnDestroy {

    userinfo: Userinfo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userinfoService: UserinfoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserinfos();
    }

    load(id) {
        this.userinfoService.find(id)
            .subscribe((userinfoResponse: HttpResponse<Userinfo>) => {
                this.userinfo = userinfoResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserinfos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userinfoListModification',
            (response) => this.load(this.userinfo.id)
        );
    }
}
