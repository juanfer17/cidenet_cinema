jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BookingService } from '../service/booking.service';
import { IBooking, Booking } from '../booking.model';
import { IFunctionFilm } from 'app/entities/function-film/function-film.model';
import { FunctionFilmService } from 'app/entities/function-film/service/function-film.service';

import { BookingUpdateComponent } from './booking-update.component';

describe('Booking Management Update Component', () => {
  let comp: BookingUpdateComponent;
  let fixture: ComponentFixture<BookingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookingService: BookingService;
  let functionFilmService: FunctionFilmService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BookingUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BookingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookingService = TestBed.inject(BookingService);
    functionFilmService = TestBed.inject(FunctionFilmService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FunctionFilm query and add missing value', () => {
      const booking: IBooking = { id: 456 };
      const functionFilm: IFunctionFilm = { id: 57170 };
      booking.functionFilm = functionFilm;

      const functionFilmCollection: IFunctionFilm[] = [{ id: 85034 }];
      jest.spyOn(functionFilmService, 'query').mockReturnValue(of(new HttpResponse({ body: functionFilmCollection })));
      const additionalFunctionFilms = [functionFilm];
      const expectedCollection: IFunctionFilm[] = [...additionalFunctionFilms, ...functionFilmCollection];
      jest.spyOn(functionFilmService, 'addFunctionFilmToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      expect(functionFilmService.query).toHaveBeenCalled();
      expect(functionFilmService.addFunctionFilmToCollectionIfMissing).toHaveBeenCalledWith(
        functionFilmCollection,
        ...additionalFunctionFilms
      );
      expect(comp.functionFilmsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const booking: IBooking = { id: 456 };
      const functionFilm: IFunctionFilm = { id: 77799 };
      booking.functionFilm = functionFilm;

      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(booking));
      expect(comp.functionFilmsSharedCollection).toContain(functionFilm);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Booking>>();
      const booking = { id: 123 };
      jest.spyOn(bookingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booking }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookingService.update).toHaveBeenCalledWith(booking);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Booking>>();
      const booking = new Booking();
      jest.spyOn(bookingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: booking }));
      saveSubject.complete();

      // THEN
      expect(bookingService.create).toHaveBeenCalledWith(booking);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Booking>>();
      const booking = { id: 123 };
      jest.spyOn(bookingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ booking });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookingService.update).toHaveBeenCalledWith(booking);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFunctionFilmById', () => {
      it('Should return tracked FunctionFilm primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFunctionFilmById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
