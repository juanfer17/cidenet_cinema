import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFunctionFilm, FunctionFilm } from '../function-film.model';
import { FunctionFilmService } from '../service/function-film.service';

@Injectable({ providedIn: 'root' })
export class FunctionFilmRoutingResolveService implements Resolve<IFunctionFilm> {
  constructor(protected service: FunctionFilmService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFunctionFilm> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((functionFilm: HttpResponse<FunctionFilm>) => {
          if (functionFilm.body) {
            return of(functionFilm.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FunctionFilm());
  }
}
