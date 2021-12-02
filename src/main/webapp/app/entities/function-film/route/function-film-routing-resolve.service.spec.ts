jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFunctionFilm, FunctionFilm } from '../function-film.model';
import { FunctionFilmService } from '../service/function-film.service';

import { FunctionFilmRoutingResolveService } from './function-film-routing-resolve.service';

describe('FunctionFilm routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FunctionFilmRoutingResolveService;
  let service: FunctionFilmService;
  let resultFunctionFilm: IFunctionFilm | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FunctionFilmRoutingResolveService);
    service = TestBed.inject(FunctionFilmService);
    resultFunctionFilm = undefined;
  });

  describe('resolve', () => {
    it('should return IFunctionFilm returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFunctionFilm = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFunctionFilm).toEqual({ id: 123 });
    });

    it('should return new IFunctionFilm if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFunctionFilm = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFunctionFilm).toEqual(new FunctionFilm());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FunctionFilm })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFunctionFilm = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFunctionFilm).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
