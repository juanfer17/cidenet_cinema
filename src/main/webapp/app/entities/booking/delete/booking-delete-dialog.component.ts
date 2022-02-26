import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooking } from '../booking.model';
import { BookingService } from '../service/booking.service';

@Component({
  templateUrl: './booking-delete-dialog.component.html',
})
export class BookingDeleteDialogComponent {
  booking?: IBooking;
  bookingSelected?: number[] = [];

  constructor(protected bookingService: BookingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(bookingSelected: number[]): void {
    // eslint-disable-next-line no-console
    console.log(bookingSelected);
    this.bookingService.delete(bookingSelected).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
