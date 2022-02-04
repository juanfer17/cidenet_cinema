import * as dayjs from 'dayjs';

export interface IFilm {
  id?: number;
  name?: string;
  genre?: string;
  urlImageContentType?: string;
  urlImage?: string;
  duration?: number;
  active?: boolean | null;
  date?: dayjs.Dayjs | null;
  description?: string;
}

export class Film implements IFilm {
  constructor(
    public id?: number,
    public name?: string,
    public genre?: string,
    public urlImageContentType?: string,
    public urlImage?: string,
    public duration?: number,
    public active?: boolean | null,
    public date?: dayjs.Dayjs | null,
    public description?: string
  ) {
    this.active = this.active ?? false;
  }
}

export function getFilmIdentifier(film: IFilm): number | undefined {
  return film.id;
}
