import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFunctionFilm, FunctionFilm } from '../function-film.model';
import { FunctionFilmService } from '../service/function-film.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { IFilm } from 'app/entities/film/film.model';
import { FilmService } from 'app/entities/film/service/film.service';

@Component({
  selector: 'jhi-function-film-update',
  templateUrl: './function-film-update.component.html',
})
export class FunctionFilmUpdateComponent implements OnInit {
  isSaving = false;

  roomsSharedCollection: IRoom[] = [];
  filmsCollection: IFilm[] = [];

  editForm = this.fb.group({
    id: [],
    dateFunction: [null, [Validators.required]],
    timeFunction: [null, [Validators.required]],
    room: [null, Validators.required],
    film: [null, Validators.required],
  });

  constructor(
    protected functionFilmService: FunctionFilmService,
    protected roomService: RoomService,
    protected filmService: FilmService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ functionFilm }) => {
      this.updateForm(functionFilm);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const functionFilm = this.createFromForm();
    if (functionFilm.id !== undefined) {
      this.subscribeToSaveResponse(this.functionFilmService.update(functionFilm));
    } else {
      this.subscribeToSaveResponse(this.functionFilmService.create(functionFilm));
    }
  }

  trackRoomById(index: number, item: IRoom): number {
    return item.id!;
  }

  trackFilmById(index: number, item: IFilm): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFunctionFilm>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(functionFilm: IFunctionFilm): void {
    this.editForm.patchValue({
      id: functionFilm.id,
      dateFunction: functionFilm.dateFunction,
      timeFunction: functionFilm.timeFunction,
      room: functionFilm.room,
      film: functionFilm.film,
    });

    this.roomsSharedCollection = this.roomService.addRoomToCollectionIfMissing(this.roomsSharedCollection, functionFilm.room);
    this.filmsCollection = this.filmService.addFilmToCollectionIfMissing(this.filmsCollection, functionFilm.film);
  }

  protected loadRelationshipsOptions(): void {
    this.roomService
      .query()
      .pipe(map((res: HttpResponse<IRoom[]>) => res.body ?? []))
      .pipe(map((rooms: IRoom[]) => this.roomService.addRoomToCollectionIfMissing(rooms, this.editForm.get('room')!.value)))
      .subscribe((rooms: IRoom[]) => (this.roomsSharedCollection = rooms));

    this.filmService
      .query({ filter: 'functionfilm-is-null' })
      .pipe(map((res: HttpResponse<IFilm[]>) => res.body ?? []))
      .pipe(map((films: IFilm[]) => this.filmService.addFilmToCollectionIfMissing(films, this.editForm.get('film')!.value)))
      .subscribe((films: IFilm[]) => (this.filmsCollection = films));
  }

  protected createFromForm(): IFunctionFilm {
    return {
      ...new FunctionFilm(),
      id: this.editForm.get(['id'])!.value,
      dateFunction: this.editForm.get(['dateFunction'])!.value,
      timeFunction: this.editForm.get(['timeFunction'])!.value,
      room: this.editForm.get(['room'])!.value,
      film: this.editForm.get(['film'])!.value,
    };
  }
}
