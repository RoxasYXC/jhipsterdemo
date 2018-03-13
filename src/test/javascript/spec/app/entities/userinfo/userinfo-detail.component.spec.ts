/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { TestTestModule } from '../../../test.module';
import { UserinfoDetailComponent } from '../../../../../../main/webapp/app/entities/userinfo/userinfo-detail.component';
import { UserinfoService } from '../../../../../../main/webapp/app/entities/userinfo/userinfo.service';
import { Userinfo } from '../../../../../../main/webapp/app/entities/userinfo/userinfo.model';

describe('Component Tests', () => {

    describe('Userinfo Management Detail Component', () => {
        let comp: UserinfoDetailComponent;
        let fixture: ComponentFixture<UserinfoDetailComponent>;
        let service: UserinfoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestTestModule],
                declarations: [UserinfoDetailComponent],
                providers: [
                    UserinfoService
                ]
            })
            .overrideTemplate(UserinfoDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserinfoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserinfoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Userinfo(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.userinfo).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
