/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TestTestModule } from '../../../test.module';
import { UserinfoComponent } from '../../../../../../main/webapp/app/entities/userinfo/userinfo.component';
import { UserinfoService } from '../../../../../../main/webapp/app/entities/userinfo/userinfo.service';
import { Userinfo } from '../../../../../../main/webapp/app/entities/userinfo/userinfo.model';

describe('Component Tests', () => {

    describe('Userinfo Management Component', () => {
        let comp: UserinfoComponent;
        let fixture: ComponentFixture<UserinfoComponent>;
        let service: UserinfoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestTestModule],
                declarations: [UserinfoComponent],
                providers: [
                    UserinfoService
                ]
            })
            .overrideTemplate(UserinfoComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserinfoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserinfoService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Userinfo(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.userinfos[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
