import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FilmComponent } from './list/film.component';
import { FilmDetailComponent } from './detail/film-detail.component';
import { FilmUpdateComponent } from './update/film-update.component';
import { FilmDeleteDialogComponent } from './delete/film-delete-dialog.component';
import { FilmRoutingModule } from './route/film-routing.module';
import { ModalSelectorComponent } from './modal-selector/modal-selector.component';

@NgModule({
  imports: [SharedModule, FilmRoutingModule],
  declarations: [FilmComponent, FilmDetailComponent, FilmUpdateComponent, FilmDeleteDialogComponent, ModalSelectorComponent],
  entryComponents: [FilmDeleteDialogComponent],
})
export class FilmModule {}
