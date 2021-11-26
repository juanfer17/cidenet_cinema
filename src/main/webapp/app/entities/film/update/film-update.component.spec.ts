jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FilmService } from '../service/film.service';
import { IFilm, Film } from '../film.model';

import { FilmUpdateComponent } from './film-update.component';

describe('Film Management Update Component', () => {
  let comp: FilmUpdateComponent;
  let fixture: ComponentFixture<FilmUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let filmService: FilmService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FilmUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FilmUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FilmUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    filmService = TestBed.inject(FilmService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const film: IFilm = { id: 456 };

      activatedRoute.data = of({ film });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(film));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Film>>();
      const film = { id: 123 };
      jest.spyOn(filmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ film });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: film }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(filmService.update).toHaveBeenCalledWith(film);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Film>>();
      const film = new Film();
      jest.spyOn(filmService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ film });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: film }));
      saveSubject.complete();

      // THEN
      expect(filmService.create).toHaveBeenCalledWith(film);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Film>>();
      const film = { id: 123 };
      jest.spyOn(filmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ film });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(filmService.update).toHaveBeenCalledWith(film);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
