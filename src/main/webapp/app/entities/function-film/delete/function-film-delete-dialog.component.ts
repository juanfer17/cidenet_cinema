import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFunctionFilm } from '../function-film.model';
import { FunctionFilmService } from '../service/function-film.service';

@Component({
  templateUrl: './function-film-delete-dialog.component.html',
})
export class FunctionFilmDeleteDialogComponent {
  functionFilm?: IFunctionFilm;

  constructor(protected functionFilmService: FunctionFilmService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.functionFilmService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
