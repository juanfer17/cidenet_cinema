import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BookingComponent } from './list/booking.component';
import { BookingDetailComponent } from './detail/booking-detail.component';
import { BookingUpdateComponent } from './update/booking-update.component';
import { BookingDeleteDialogComponent } from './delete/booking-delete-dialog.component';
import { BookingRoutingModule } from './route/booking-routing.module';
import { DeleteAllComponent } from './delete-all/delete-all.component';

@NgModule({
  imports: [SharedModule, BookingRoutingModule],
  declarations: [BookingComponent, BookingDetailComponent, BookingUpdateComponent, BookingDeleteDialogComponent, DeleteAllComponent],
  entryComponents: [BookingDeleteDialogComponent],
})
export class BookingModule {}
