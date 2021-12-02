import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'film',
        data: { pageTitle: 'cinemaCidenetApp.film.home.title' },
        loadChildren: () => import('./film/film.module').then(m => m.FilmModule),
      },
      {
        path: 'room',
        data: { pageTitle: 'cinemaCidenetApp.room.home.title' },
        loadChildren: () => import('./room/room.module').then(m => m.RoomModule),
      },
      {
        path: 'function-film',
        data: { pageTitle: 'cinemaCidenetApp.functionFilm.home.title' },
        loadChildren: () => import('./function-film/function-film.module').then(m => m.FunctionFilmModule),
      },
      {
        path: 'booking',
        data: { pageTitle: 'cinemaCidenetApp.booking.home.title' },
        loadChildren: () => import('./booking/booking.module').then(m => m.BookingModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
