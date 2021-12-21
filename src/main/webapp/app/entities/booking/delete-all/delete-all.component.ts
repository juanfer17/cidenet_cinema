import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBooking } from '../booking.model';
import { BookingService } from '../service/booking.service';

@Component({
  templateUrl: './delete-all.component.html',
})
export class DeleteAllComponent {
  booking?: IBooking;
  constructor(protected bookingService: BookingService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookingService.deleteAll(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
