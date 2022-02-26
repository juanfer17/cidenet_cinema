import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooking } from '../booking.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { BookingService } from '../service/booking.service';
import { BookingDeleteDialogComponent } from '../delete/booking-delete-dialog.component';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { DeleteAllComponent } from '../delete-all/delete-all.component';
import { FilmService } from 'app/entities/film/service/film.service';
import { FunctionFilmService } from 'app/entities/function-film/service/function-film.service';
import { IFilm } from 'app/entities/film/film.model';
import { IFunctionFilm } from 'app/entities/function-film/function-film.model';
import { IBookingByUser } from './bookingByUser.model';

@Component({
  selector: 'jhi-booking',
  templateUrl: './booking.component.html',
  styleUrls: ['./booking.component.scss'],
})
export class BookingComponent implements OnInit {
  bookings?: IBookingByUser[];
  bookingSelected?: number[] = [];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  account: Account | null = null;
  functionFilms?: IFunctionFilm[];

  private readonly destroy$ = new Subject<void>();

  constructor(
    protected bookingService: BookingService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    private accountService: AccountService,
    protected functionFilmService: FunctionFilmService
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    const user = this.account?.login;

    if (user !== undefined) {
      this.bookingService
        .queryByUser(user, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IBookingByUser[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
    }
  }

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.handleNavigation();
  }

  trackId(index: number, item: IBooking): number {
    return item.id!;
  }

  chairSelected(chairSelectedLocation: any): void {
    const index = this.bookingSelected?.indexOf(chairSelectedLocation);
    if (index !== undefined) {
      if (index > -1) {
        this.bookingSelected?.splice(index, 1); // 2nd parametro significa que solo remueve un elemento
      } else {
        this.bookingSelected?.push(chairSelectedLocation);
      }
    }

    // eslint-disable-next-line no-console
    console.log(this.bookingSelected);
    // eslint-disable-next-line no-console
    console.log('Este es el tamaño del booking para eliminar');
  }

  delete(): void {
    const modalRef = this.modalService.open(BookingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bookingSelected = this.bookingSelected;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  deleteAll(booking: IBookingByUser): void {
    // eslint-disable-next-line no-console
    console.log(booking);
    const modalRef = this.modalService.open(DeleteAllComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.booking = booking.chairList?.[0];
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IBookingByUser[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/booking'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.bookings = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
