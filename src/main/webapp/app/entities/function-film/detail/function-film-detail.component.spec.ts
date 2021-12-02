import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FunctionFilmDetailComponent } from './function-film-detail.component';

describe('FunctionFilm Management Detail Component', () => {
  let comp: FunctionFilmDetailComponent;
  let fixture: ComponentFixture<FunctionFilmDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FunctionFilmDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ functionFilm: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FunctionFilmDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FunctionFilmDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load functionFilm on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.functionFilm).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
