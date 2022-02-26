import { FunctionFilm } from 'app/entities/function-film/function-film.model';
import { IBooking } from '../booking.model';

export interface IBookingByUser {
  functionFilmData?: FunctionFilm;
  chairList?: IBooking[];
}
