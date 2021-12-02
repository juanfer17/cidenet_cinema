import { IUser } from 'app/entities/user/user.model';
import { IFunctionFilm } from 'app/entities/function-film/function-film.model';

export interface IBooking {
  id?: number;
  chairLocation?: string;
  status?: string;
  user?: IUser;
  functionFilm?: IFunctionFilm;
}

export class Booking implements IBooking {
  constructor(
    public id?: number,
    public chairLocation?: string,
    public status?: string,
    public user?: IUser,
    public functionFilm?: IFunctionFilm
  ) {}
}

export function getBookingIdentifier(booking: IBooking): number | undefined {
  return booking.id;
}
