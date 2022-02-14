import * as dayjs from 'dayjs';
import { IRoom } from 'app/entities/room/room.model';
import { IFilm } from 'app/entities/film/film.model';

export interface IFunctionFilm {
  id?: number;
  dateFunction?: dayjs.Dayjs;
  timeFunction?: string;
  room?: IRoom;
  film?: IFilm;
}

export class FunctionFilm implements IFunctionFilm {
  constructor(
    public id?: number,
    public dateFunction?: dayjs.Dayjs,
    public timeFunction?: string,
    public room?: IRoom,
    public film?: IFilm
  ) {}
}

export function getFunctionFilmIdentifier(functionFilm: IFunctionFilm): number | undefined {
  return functionFilm.id;
}
