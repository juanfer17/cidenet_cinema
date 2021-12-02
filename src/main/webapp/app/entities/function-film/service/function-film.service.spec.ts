import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFunctionFilm, FunctionFilm } from '../function-film.model';

import { FunctionFilmService } from './function-film.service';

describe('FunctionFilm Service', () => {
  let service: FunctionFilmService;
  let httpMock: HttpTestingController;
  let elemDefault: IFunctionFilm;
  let expectedResult: IFunctionFilm | IFunctionFilm[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FunctionFilmService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateFunction: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateFunction: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FunctionFilm', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateFunction: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFunction: currentDate,
        },
        returnedFromService
      );

      service.create(new FunctionFilm()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FunctionFilm', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateFunction: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFunction: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FunctionFilm', () => {
      const patchObject = Object.assign(
        {
          dateFunction: currentDate.format(DATE_FORMAT),
        },
        new FunctionFilm()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateFunction: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FunctionFilm', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateFunction: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFunction: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FunctionFilm', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFunctionFilmToCollectionIfMissing', () => {
      it('should add a FunctionFilm to an empty array', () => {
        const functionFilm: IFunctionFilm = { id: 123 };
        expectedResult = service.addFunctionFilmToCollectionIfMissing([], functionFilm);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(functionFilm);
      });

      it('should not add a FunctionFilm to an array that contains it', () => {
        const functionFilm: IFunctionFilm = { id: 123 };
        const functionFilmCollection: IFunctionFilm[] = [
          {
            ...functionFilm,
          },
          { id: 456 },
        ];
        expectedResult = service.addFunctionFilmToCollectionIfMissing(functionFilmCollection, functionFilm);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FunctionFilm to an array that doesn't contain it", () => {
        const functionFilm: IFunctionFilm = { id: 123 };
        const functionFilmCollection: IFunctionFilm[] = [{ id: 456 }];
        expectedResult = service.addFunctionFilmToCollectionIfMissing(functionFilmCollection, functionFilm);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(functionFilm);
      });

      it('should add only unique FunctionFilm to an array', () => {
        const functionFilmArray: IFunctionFilm[] = [{ id: 123 }, { id: 456 }, { id: 25635 }];
        const functionFilmCollection: IFunctionFilm[] = [{ id: 123 }];
        expectedResult = service.addFunctionFilmToCollectionIfMissing(functionFilmCollection, ...functionFilmArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const functionFilm: IFunctionFilm = { id: 123 };
        const functionFilm2: IFunctionFilm = { id: 456 };
        expectedResult = service.addFunctionFilmToCollectionIfMissing([], functionFilm, functionFilm2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(functionFilm);
        expect(expectedResult).toContain(functionFilm2);
      });

      it('should accept null and undefined values', () => {
        const functionFilm: IFunctionFilm = { id: 123 };
        expectedResult = service.addFunctionFilmToCollectionIfMissing([], null, functionFilm, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(functionFilm);
      });

      it('should return initial array if no FunctionFilm is added', () => {
        const functionFilmCollection: IFunctionFilm[] = [{ id: 123 }];
        expectedResult = service.addFunctionFilmToCollectionIfMissing(functionFilmCollection, undefined, null);
        expect(expectedResult).toEqual(functionFilmCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
