import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FunctionFilmComponent } from '../list/function-film.component';
import { FunctionFilmDetailComponent } from '../detail/function-film-detail.component';
import { FunctionFilmUpdateComponent } from '../update/function-film-update.component';
import { FunctionFilmRoutingResolveService } from './function-film-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const functionFilmRoute: Routes = [
  {
    path: '',
    component: FunctionFilmComponent,
    data: {
      defaultSort: 'id,asc',
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FunctionFilmDetailComponent,
    resolve: {
      functionFilm: FunctionFilmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    data: {
      authorities: [Authority.ADMIN],
    },
    component: FunctionFilmUpdateComponent,
    resolve: {
      functionFilm: FunctionFilmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    data: {
      authorities: [Authority.ADMIN],
    },
    component: FunctionFilmUpdateComponent,
    resolve: {
      functionFilm: FunctionFilmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(functionFilmRoute)],
  exports: [RouterModule],
})
export class FunctionFilmRoutingModule {}
