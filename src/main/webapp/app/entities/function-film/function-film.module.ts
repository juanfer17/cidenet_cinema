import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FunctionFilmComponent } from './list/function-film.component';
import { FunctionFilmDetailComponent } from './detail/function-film-detail.component';
import { FunctionFilmUpdateComponent } from './update/function-film-update.component';
import { FunctionFilmDeleteDialogComponent } from './delete/function-film-delete-dialog.component';
import { FunctionFilmRoutingModule } from './route/function-film-routing.module';

@NgModule({
  imports: [SharedModule, FunctionFilmRoutingModule],
  declarations: [FunctionFilmComponent, FunctionFilmDetailComponent, FunctionFilmUpdateComponent, FunctionFilmDeleteDialogComponent],
  entryComponents: [FunctionFilmDeleteDialogComponent],
})
export class FunctionFilmModule {}
