import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FunctionFilmComponent } from '../list/function-film.component';
import { FunctionFilmDetailComponent } from '../detail/function-film-detail.component';
import { FunctionFilmUpdateComponent } from '../update/function-film-update.component';
import { FunctionFilmRoutingResolveService } from './function-film-routing-resolve.service';

const functionFilmRoute: Routes = [
  {
    path: '',
    component: FunctionFilmComponent,
    data: {
      defaultSort: 'id,asc',
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
    component: FunctionFilmUpdateComponent,
    resolve: {
      functionFilm: FunctionFilmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
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
