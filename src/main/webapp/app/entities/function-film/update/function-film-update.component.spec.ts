jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FunctionFilmService } from '../service/function-film.service';
import { IFunctionFilm, FunctionFilm } from '../function-film.model';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { IFilm } from 'app/entities/film/film.model';
import { FilmService } from 'app/entities/film/service/film.service';

import { FunctionFilmUpdateComponent } from './function-film-update.component';

describe('FunctionFilm Management Update Component', () => {
  let comp: FunctionFilmUpdateComponent;
  let fixture: ComponentFixture<FunctionFilmUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let functionFilmService: FunctionFilmService;
  let roomService: RoomService;
  let filmService: FilmService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FunctionFilmUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FunctionFilmUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FunctionFilmUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    functionFilmService = TestBed.inject(FunctionFilmService);
    roomService = TestBed.inject(RoomService);
    filmService = TestBed.inject(FilmService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Room query and add missing value', () => {
      const functionFilm: IFunctionFilm = { id: 456 };
      const room: IRoom = { id: 65928 };
      functionFilm.room = room;

      const roomCollection: IRoom[] = [{ id: 91451 }];
      jest.spyOn(roomService, 'query').mockReturnValue(of(new HttpResponse({ body: roomCollection })));
      const additionalRooms = [room];
      const expectedCollection: IRoom[] = [...additionalRooms, ...roomCollection];
      jest.spyOn(roomService, 'addRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ functionFilm });
      comp.ngOnInit();

      expect(roomService.query).toHaveBeenCalled();
      expect(roomService.addRoomToCollectionIfMissing).toHaveBeenCalledWith(roomCollection, ...additionalRooms);
      expect(comp.roomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Film query and add missing value', () => {
      const functionFilm: IFunctionFilm = { id: 456 };
      const film: IFilm = { id: 12545 };
      functionFilm.film = film;

      const filmCollection: IFilm[] = [{ id: 63970 }];
      jest.spyOn(filmService, 'query').mockReturnValue(of(new HttpResponse({ body: filmCollection })));
      const additionalFilms = [film];
      const expectedCollection: IFilm[] = [...additionalFilms, ...filmCollection];
      jest.spyOn(filmService, 'addFilmToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ functionFilm });
      comp.ngOnInit();

      expect(filmService.query).toHaveBeenCalled();
      expect(filmService.addFilmToCollectionIfMissing).toHaveBeenCalledWith(filmCollection, ...additionalFilms);
      expect(comp.filmsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const functionFilm: IFunctionFilm = { id: 456 };
      const room: IRoom = { id: 17528 };
      functionFilm.room = room;
      const film: IFilm = { id: 3993 };
      functionFilm.film = film;

      activatedRoute.data = of({ functionFilm });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(functionFilm));
      expect(comp.roomsSharedCollection).toContain(room);
      expect(comp.filmsSharedCollection).toContain(film);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FunctionFilm>>();
      const functionFilm = { id: 123 };
      jest.spyOn(functionFilmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ functionFilm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: functionFilm }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(functionFilmService.update).toHaveBeenCalledWith(functionFilm);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FunctionFilm>>();
      const functionFilm = new FunctionFilm();
      jest.spyOn(functionFilmService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ functionFilm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: functionFilm }));
      saveSubject.complete();

      // THEN
      expect(functionFilmService.create).toHaveBeenCalledWith(functionFilm);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FunctionFilm>>();
      const functionFilm = { id: 123 };
      jest.spyOn(functionFilmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ functionFilm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(functionFilmService.update).toHaveBeenCalledWith(functionFilm);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRoomById', () => {
      it('Should return tracked Room primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRoomById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFilmById', () => {
      it('Should return tracked Film primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFilmById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
