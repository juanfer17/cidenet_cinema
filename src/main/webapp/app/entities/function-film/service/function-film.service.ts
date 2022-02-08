import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFunctionFilm, getFunctionFilmIdentifier } from '../function-film.model';

export type EntityResponseType = HttpResponse<IFunctionFilm>;
export type EntityArrayResponseType = HttpResponse<IFunctionFilm[]>;

@Injectable({ providedIn: 'root' })
export class FunctionFilmService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/function-films');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(functionFilm: IFunctionFilm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(functionFilm);
    return this.http
      .post<IFunctionFilm>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(functionFilm: IFunctionFilm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(functionFilm);
    return this.http
      .put<IFunctionFilm>(`${this.resourceUrl}/${getFunctionFilmIdentifier(functionFilm) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(functionFilm: IFunctionFilm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(functionFilm);
    return this.http
      .patch<IFunctionFilm>(`${this.resourceUrl}/${getFunctionFilmIdentifier(functionFilm) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFunctionFilm>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFunctionFilm[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFunctionFilmToCollectionIfMissing(
    functionFilmCollection: IFunctionFilm[],
    ...functionFilmsToCheck: (IFunctionFilm | null | undefined)[]
  ): IFunctionFilm[] {
    const functionFilms: IFunctionFilm[] = functionFilmsToCheck.filter(isPresent);
    if (functionFilms.length > 0) {
      const functionFilmCollectionIdentifiers = functionFilmCollection.map(
        functionFilmItem => getFunctionFilmIdentifier(functionFilmItem)!
      );
      const functionFilmsToAdd = functionFilms.filter(functionFilmItem => {
        const functionFilmIdentifier = getFunctionFilmIdentifier(functionFilmItem);
        if (functionFilmIdentifier == null || functionFilmCollectionIdentifiers.includes(functionFilmIdentifier)) {
          return false;
        }
        functionFilmCollectionIdentifiers.push(functionFilmIdentifier);
        return true;
      });
      return [...functionFilmsToAdd, ...functionFilmCollection];
    }
    return functionFilmCollection;
  }

  findByFilm(req: number): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFunctionFilm[]>(`${this.resourceUrl}/view/${req}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findByDate(req: any): Observable<EntityArrayResponseType> {
    const copy = this.convertDateFromClient(req);
    return this.http
      .post<IFunctionFilm[]>(`${this.resourceUrl}/date`, copy, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(functionFilm: IFunctionFilm): IFunctionFilm {
    return Object.assign({}, functionFilm, {
      dateFunction: functionFilm.dateFunction?.isValid() ? functionFilm.dateFunction.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateFunction = res.body.dateFunction ? dayjs(res.body.dateFunction) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((functionFilm: IFunctionFilm) => {
        functionFilm.dateFunction = functionFilm.dateFunction ? dayjs(functionFilm.dateFunction) : undefined;
      });
    }
    return res;
  }
}
