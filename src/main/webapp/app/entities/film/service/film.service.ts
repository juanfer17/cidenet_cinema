import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFilm, getFilmIdentifier } from '../film.model';

export type EntityResponseType = HttpResponse<IFilm>;
export type EntityArrayResponseType = HttpResponse<IFilm[]>;

@Injectable({ providedIn: 'root' })
export class FilmService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/films');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(film: IFilm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(film);
    return this.http
      .post<IFilm>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(film: IFilm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(film);
    return this.http
      .put<IFilm>(`${this.resourceUrl}/${getFilmIdentifier(film) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(film: IFilm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(film);
    return this.http
      .patch<IFilm>(`${this.resourceUrl}/${getFilmIdentifier(film) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFilm>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFilm[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFilmToCollectionIfMissing(filmCollection: IFilm[], ...filmsToCheck: (IFilm | null | undefined)[]): IFilm[] {
    const films: IFilm[] = filmsToCheck.filter(isPresent);
    if (films.length > 0) {
      const filmCollectionIdentifiers = filmCollection.map(filmItem => getFilmIdentifier(filmItem)!);
      const filmsToAdd = films.filter(filmItem => {
        const filmIdentifier = getFilmIdentifier(filmItem);
        if (filmIdentifier == null || filmCollectionIdentifiers.includes(filmIdentifier)) {
          return false;
        }
        filmCollectionIdentifiers.push(filmIdentifier);
        return true;
      });
      return [...filmsToAdd, ...filmCollection];
    }
    return filmCollection;
  }

  protected convertDateFromClient(film: IFilm): IFilm {
    return Object.assign({}, film, {
      date: film.date?.isValid() ? film.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((film: IFilm) => {
        film.date = film.date ? dayjs(film.date) : undefined;
      });
    }
    return res;
  }
}
