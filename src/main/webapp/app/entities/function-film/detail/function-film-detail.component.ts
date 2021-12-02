import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFunctionFilm } from '../function-film.model';

@Component({
  selector: 'jhi-function-film-detail',
  templateUrl: './function-film-detail.component.html',
})
export class FunctionFilmDetailComponent implements OnInit {
  functionFilm: IFunctionFilm | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ functionFilm }) => {
      this.functionFilm = functionFilm;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
