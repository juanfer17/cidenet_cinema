export interface IRoom {
  id?: number;
  roomName?: string;
  roomType?: string;
  row?: number;
  column?: number;
  statusRoom?: boolean;
  bookingPrice?: number;
}

export class Room implements IRoom {
  constructor(
    public id?: number,
    public roomName?: string,
    public roomType?: string,
    public row?: number,
    public column?: number,
    public statusRoom?: boolean,
    public bookingPrice?: number
  ) {
    this.statusRoom = this.statusRoom ?? false;
  }
}

export function getRoomIdentifier(room: IRoom): number | undefined {
  return room.id;
}
