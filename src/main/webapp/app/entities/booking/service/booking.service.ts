import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooking, getBookingIdentifier } from '../booking.model';
import { IBookingByUser } from '../list/bookingByUser.model';

export type EntityResponseType = HttpResponse<IBooking>;
export type EntityArrayResponseType = HttpResponse<IBooking[]>;
export type EntityArrayResponseTypeByUser = HttpResponse<IBookingByUser[]>;

@Injectable({ providedIn: 'root' })
export class BookingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bookings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(booking: IBooking): Observable<EntityResponseType> {
    return this.http.post<IBooking>(this.resourceUrl, booking, { observe: 'response' });
  }

  update(booking: IBooking): Observable<EntityResponseType> {
    return this.http.put<IBooking>(`${this.resourceUrl}/${getBookingIdentifier(booking) as number}`, booking, { observe: 'response' });
  }

  partialUpdate(booking: IBooking): Observable<EntityResponseType> {
    return this.http.patch<IBooking>(`${this.resourceUrl}/${getBookingIdentifier(booking) as number}`, booking, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBooking>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooking[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number[]): Observable<HttpResponse<{}>> {
    return this.http.post(`${this.resourceUrl}/delete`, id, { observe: 'response' });
  }

  createBookingList(booking: IBooking[]): Observable<EntityArrayResponseType> {
    return this.http.post<IBooking[]>(`${this.resourceUrl}/confirm`, booking, { observe: 'response' });
  }

  addBookingToCollectionIfMissing(bookingCollection: IBooking[], ...bookingsToCheck: (IBooking | null | undefined)[]): IBooking[] {
    const bookings: IBooking[] = bookingsToCheck.filter(isPresent);
    if (bookings.length > 0) {
      const bookingCollectionIdentifiers = bookingCollection.map(bookingItem => getBookingIdentifier(bookingItem)!);
      const bookingsToAdd = bookings.filter(bookingItem => {
        const bookingIdentifier = getBookingIdentifier(bookingItem);
        if (bookingIdentifier == null || bookingCollectionIdentifiers.includes(bookingIdentifier)) {
          return false;
        }
        bookingCollectionIdentifiers.push(bookingIdentifier);
        return true;
      });
      return [...bookingsToAdd, ...bookingCollection];
    }
    return bookingCollection;
  }

  bookingByFunction(req: number): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBooking[]>(`${this.resourceUrl}/byFunction/${req}`, { params: options, observe: 'response' });
  }

  queryByUser(user: string, req?: any): Observable<EntityArrayResponseTypeByUser> {
    const options = createRequestOption(req);
    return this.http.get<IBookingByUser[]>(`${this.resourceUrl}/user/${user}`, { params: options, observe: 'response' });
  }

  deleteAll(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/all/${id}`, { observe: 'response' });
  }
}
