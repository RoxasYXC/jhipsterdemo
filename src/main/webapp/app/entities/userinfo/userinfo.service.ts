import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Userinfo } from './userinfo.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Userinfo>;

@Injectable()
export class UserinfoService {

    private resourceUrl =  SERVER_API_URL + 'api/userinfos';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/userinfos';

    constructor(private http: HttpClient) { }

    create(userinfo: Userinfo): Observable<EntityResponseType> {
        const copy = this.convert(userinfo);
        return this.http.post<Userinfo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(userinfo: Userinfo): Observable<EntityResponseType> {
        const copy = this.convert(userinfo);
        return this.http.put<Userinfo>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Userinfo>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Userinfo[]>> {
        const options = createRequestOption(req);
        return this.http.get<Userinfo[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Userinfo[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Userinfo[]>> {
        const options = createRequestOption(req);
        return this.http.get<Userinfo[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Userinfo[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Userinfo = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Userinfo[]>): HttpResponse<Userinfo[]> {
        const jsonResponse: Userinfo[] = res.body;
        const body: Userinfo[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Userinfo.
     */
    private convertItemFromServer(userinfo: Userinfo): Userinfo {
        const copy: Userinfo = Object.assign({}, userinfo);
        return copy;
    }

    /**
     * Convert a Userinfo to a JSON which can be sent to the server.
     */
    private convert(userinfo: Userinfo): Userinfo {
        const copy: Userinfo = Object.assign({}, userinfo);
        return copy;
    }
}
