import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFilm } from '../film.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { FunctionFilmService } from 'app/entities/function-film/service/function-film.service';
import { IFunctionFilm } from 'app/entities/function-film/function-film.model';
import { BookingService } from 'app/entities/booking/service/booking.service';
import { IBooking } from 'app/entities/booking/booking.model';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { getRoomIdentifier, IRoom, Room } from 'app/entities/room/room.model';
import { FormBuilder } from '@angular/forms';
import { RoomService } from 'app/entities/room/service/room.service';
import { FilmService } from '../service/film.service';
import { NgbDate, NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FilmDeleteDialogComponent } from '../delete/film-delete-dialog.component';

@Component({
  selector: 'jhi-film-detail',
  templateUrl: './film-detail.component.html',
  styleUrls: ['./film-detail.component.scss'],
})
export class FilmDetailComponent implements OnInit {
  film: IFilm | null = null;
  functionFilms: IFunctionFilm[] = [];
  bookings?: IBooking[];
  bookingSelected?: IBooking[] = [];
  account: Account | null = null;
  success = false;
  totalPrice = 0;
  unitPrice: any = 0;
  bookingLength: any = 0;
  year = 0;
  day = 0;
  month = 0;
  minDate: any = '';
  maxDate: any = '';
  show = false;

  editForm = this.fb.group({
    id: [],
    dateFunction: [null],
  });

  private readonly destroy$ = new Subject<void>();

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected functionFilmService: FunctionFilmService,
    protected bookingService: BookingService,
    private accountService: AccountService,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ film }) => {
      this.film = film;
    });

    const now = new Date();
    this.year = now.getFullYear();
    this.month = now.getMonth();
    this.day = now.getDate();
    this.minDate = { year: this.year, month: this.month + 1, day: this.day };
    this.maxDate = { year: this.year, month: this.month + 3, day: this.day + 10 };

    // eslint-disable-next-line no-console
    console.log('Aqui va el año');
    // eslint-disable-next-line no-console
    console.log('year:', this.year, 'month:', this.month, 'day:', this.day);

    // this.queryFunctionByFilm();

    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  queryFunctionByFilm(): void {
    const filmId = this.film?.id;
    if (filmId !== undefined) {
      const dataRequest = {
        idFilm: filmId,
        dateFunction: this.editForm.get(['dateFunction'])!.value,
      };
      // eslint-disable-next-line no-console
      console.log('Aqui va el año');
      // eslint-disable-next-line no-console
      console.log(dataRequest.dateFunction);

      this.functionFilmService.findByDate(dataRequest).subscribe(dataResponse => {
        if (dataResponse.body !== null) {
          this.functionFilms = dataResponse.body;
          this.unitPrice = this.functionFilms[0].room?.bookingPrice;
        }
      });
    }
  }

  queryChairByFunction(idFunction?: number): void {
    const modalRef = this.modalService.open(FilmDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.idFunction = idFunction;

    /* this.showModal(); */
    if (idFunction !== undefined) {
      this.bookingService.bookingByFunction(idFunction).subscribe(dataResponse => {
        if (dataResponse.body !== null) {
          this.bookings = dataResponse.body;
        }
      });
    }
  }

  chairSelected(chairSelectedLocation: IBooking): void {
    chairSelectedLocation.login = this.account?.login;

    // eslint-disable-next-line no-console
    console.log(chairSelectedLocation.login);

    const index = this.bookingSelected?.indexOf(chairSelectedLocation);
    if (index !== undefined) {
      if (index > -1) {
        this.bookingSelected?.splice(index, 1); // 2nd parametro significa que solo remueve un elemento
      } else {
        this.bookingSelected?.push(chairSelectedLocation);
      }
    }

    // eslint-disable-next-line no-console
    console.log(this.bookingSelected?.length);

    this.bookingLength = this.bookingSelected?.length;

    if (this.bookingLength !== undefined) {
      // eslint-disable-next-line no-console
      console.log(this.unitPrice * this.bookingLength);

      this.totalPrice = this.unitPrice * this.bookingLength;
    }
  }

  clean(): void {
    this.totalPrice = 0;
    this.bookingLength = 0;
    this.bookingSelected = [];
  }
  confirmChairSelected(): void {
    if (this.bookingSelected !== undefined) {
      this.bookingService.createBookingList(this.bookingSelected).subscribe(dataResponse => {
        // eslint-disable-next-line no-console
        console.log(`Sillas reservadas`);

        this.clean();

        this.success = true;
      });
    }
  }

  showModal(): void {
    this.show = true;
  }

  hidenModal(): void {
    this.show = false;
  }
}
