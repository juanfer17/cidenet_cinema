import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { IFilm } from 'app/entities/film/film.model';
import { FilmService } from 'app/entities/film/service/film.service';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  films?: IFilm[] | null = [];
  page?: number;
  itemsPerPage = ITEMS_PER_PAGE;
  predicate!: string;
  ascending!: boolean;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, protected filmService: FilmService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    // eslint-disable-next-line no-console
    console.log('Antes del servicio');
    this.filmList();
    // eslint-disable-next-line no-console
    console.log('Despues del servicio');
  }

  filmList(): void {
    // eslint-disable-next-line no-console
    console.log('Dentro del servicio');
    this.filmService.queryActive().subscribe(dataResponse => {
      this.films = dataResponse.body;
    });
    // eslint-disable-next-line no-console
    console.log('Saliendo del servicio');
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
