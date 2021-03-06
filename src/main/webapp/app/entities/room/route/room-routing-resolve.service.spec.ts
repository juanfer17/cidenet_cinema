jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRoom, Room } from '../room.model';
import { RoomService } from '../service/room.service';

import { RoomRoutingResolveService } from './room-routing-resolve.service';

describe('Room routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RoomRoutingResolveService;
  let service: RoomService;
  let resultRoom: IRoom | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RoomRoutingResolveService);
    service = TestBed.inject(RoomService);
    resultRoom = undefined;
  });

  describe('resolve', () => {
    it('should return IRoom returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRoom = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRoom).toEqual({ id: 123 });
    });

    it('should return new IRoom if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRoom = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRoom).toEqual(new Room());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Room })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRoom = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRoom).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
