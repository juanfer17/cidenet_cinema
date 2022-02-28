import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AccountService } from 'app/core/auth/account.service';
import { Booking, IBooking } from 'app/entities/booking/booking.model';
import { BookingService } from 'app/entities/booking/service/booking.service';
import { FunctionFilm, IFunctionFilm } from 'app/entities/function-film/function-film.model';
import { FilmDetailComponent } from '../detail/film-detail.component';
import { IFilm } from '../film.model';
import { FilmService } from '../service/film.service';

@Component({
  selector: 'jhi-modal-selector',
  templateUrl: './modal-selector.component.html',
  styleUrls: ['./modal-selector.component.scss'],
})
export class ModalSelectorComponent {
  film?: IFilm;
  bookings?: IBooking[] = [];
  bookingSelected?: IBooking[] = [];
  success = false;
  totalPrice = 0;
  unitPrice: any = 0;
  bookingLength: any = 0;
  show = false;
  idFunction?: IFunctionFilm[] = [];

  constructor(
    protected filmService: FilmService,
    protected activeModal: NgbActiveModal,
    protected bookingService: BookingService,
    private accountService: AccountService,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    // eslint-disable-next-line no-console
    console.log('Booking select en mmodal-selector');
    // eslint-disable-next-line no-console
    console.log(this.bookings);
  }

  chairSelected(chairSelectedLocation: IBooking): void {
    const index = this.bookingSelected?.indexOf(chairSelectedLocation);
    if (index !== undefined) {
      if (index > -1) {
        this.bookingSelected?.splice(index, 1); // 2nd parametro significa que solo remueve un elemento
      } else {
        this.bookingSelected?.push(chairSelectedLocation);
      }
    }
    this.bookingLength = this.bookingSelected?.length;
    if (this.bookingLength !== undefined) {
      // eslint-disable-next-line no-console
      console.log(this.unitPrice * this.bookingLength);
      this.totalPrice = this.unitPrice * this.bookingLength;
    }
  }

  confirmChairSelected(): void {
    // eslint-disable-next-line no-console
    console.log('Booking select en mmodal-selector');
    // eslint-disable-next-line no-console
    console.log(this.bookingSelected);
    if (this.bookingSelected !== undefined) {
      this.bookingService.createBookingList(this.bookingSelected).subscribe(dataResponse => {
        // eslint-disable-next-line no-console
        console.log(`Sillas reservadas`);

        this.clean();

        this.success = true;
      });
    }
  }

  clean(): void {
    this.totalPrice = 0;
    this.bookingLength = 0;
    this.bookingSelected = [];
  }

  cancel(): void {
    this.clean();
    this.activeModal.dismiss();
  }
  /*   confirmDelete(id: number): void {
    this.filmService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  } */
}
