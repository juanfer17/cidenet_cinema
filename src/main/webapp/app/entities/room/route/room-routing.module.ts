import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoomComponent } from '../list/room.component';
import { RoomDetailComponent } from '../detail/room-detail.component';
import { RoomUpdateComponent } from '../update/room-update.component';
import { RoomRoutingResolveService } from './room-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const roomRoute: Routes = [
  {
    path: '',
    component: RoomComponent,
    data: {
      defaultSort: 'id,asc',
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomDetailComponent,
    resolve: {
      room: RoomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    data: {
      authorities: [Authority.ADMIN],
    },
    component: RoomUpdateComponent,
    resolve: {
      room: RoomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    data: {
      authorities: [Authority.ADMIN],
    },
    component: RoomUpdateComponent,
    resolve: {
      room: RoomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomRoute)],
  exports: [RouterModule],
})
export class RoomRoutingModule {}
