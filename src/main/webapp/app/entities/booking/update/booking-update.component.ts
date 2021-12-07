import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBooking, Booking } from '../booking.model';
import { BookingService } from '../service/booking.service';
import { IFunctionFilm } from 'app/entities/function-film/function-film.model';
import { FunctionFilmService } from 'app/entities/function-film/service/function-film.service';

@Component({
  selector: 'jhi-booking-update',
  templateUrl: './booking-update.component.html',
})
export class BookingUpdateComponent implements OnInit {
  isSaving = false;

  functionFilmsSharedCollection: IFunctionFilm[] = [];

  editForm = this.fb.group({
    id: [],
    chairLocation: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(4)]],
    status: [null, [Validators.required]],
    user: [],
    functionFilm: [null, Validators.required],
  });

  constructor(
    protected bookingService: BookingService,
    protected functionFilmService: FunctionFilmService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booking }) => {
      this.updateForm(booking);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booking = this.createFromForm();
    if (booking.id !== undefined) {
      this.subscribeToSaveResponse(this.bookingService.update(booking));
    } else {
      this.subscribeToSaveResponse(this.bookingService.create(booking));
    }
  }

  trackFunctionFilmById(index: number, item: IFunctionFilm): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooking>>): void {
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

  protected updateForm(booking: IBooking): void {
    this.editForm.patchValue({
      id: booking.id,
      chairLocation: booking.chairLocation,
      status: booking.status,
      user: booking.user,
      functionFilm: booking.functionFilm,
    });

    this.functionFilmsSharedCollection = this.functionFilmService.addFunctionFilmToCollectionIfMissing(
      this.functionFilmsSharedCollection,
      booking.functionFilm
    );
  }

  protected loadRelationshipsOptions(): void {
    this.functionFilmService
      .query()
      .pipe(map((res: HttpResponse<IFunctionFilm[]>) => res.body ?? []))
      .pipe(
        map((functionFilms: IFunctionFilm[]) =>
          this.functionFilmService.addFunctionFilmToCollectionIfMissing(functionFilms, this.editForm.get('functionFilm')!.value)
        )
      )
      .subscribe((functionFilms: IFunctionFilm[]) => (this.functionFilmsSharedCollection = functionFilms));
  }

  protected createFromForm(): IBooking {
    return {
      ...new Booking(),
      id: this.editForm.get(['id'])!.value,
      chairLocation: this.editForm.get(['chairLocation'])!.value,
      status: this.editForm.get(['status'])!.value,
      user: this.editForm.get(['user'])!.value,
      functionFilm: this.editForm.get(['functionFilm'])!.value,
    };
  }
}
