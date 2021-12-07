import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFilm } from '../film.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { FunctionFilmService } from 'app/entities/function-film/service/function-film.service';
import { IFunctionFilm } from 'app/entities/function-film/function-film.model';

@Component({
  selector: 'jhi-film-detail',
  templateUrl: './film-detail.component.html',
  styleUrls: ['./film-detail.component.scss'],
})
export class FilmDetailComponent implements OnInit {
  film: IFilm | null = null;
  functionFilms?: IFunctionFilm[];

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected functionFilmService: FunctionFilmService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ film }) => {
      this.film = film;
    });
    this.queryFunctionByFilm();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  queryFunctionByFilm(): void {
    const idFilm = this.film?.id;
    if (idFilm !== undefined) {
      this.functionFilmService.findByFilm(idFilm).subscribe(dataResponse => {
        if (dataResponse.body !== null) {
          this.functionFilms = dataResponse.body;
        }
      });
    }
  }
}
