import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { catchError, concat, debounceTime, distinctUntilChanged, Observable, of, Subject, switchMap } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { GeneralSearchResponseDto } from '../../dtos/common/GeneralSearchResponseDto';
import { GeneralSearchService } from '../../services/general-search/general-search.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class HeaderComponent {

  search$: Observable<GeneralSearchResponseDto[]>;
  searchLoading = false;
  searchInput$ = new Subject<string>();
  selectedSearchItem: GeneralSearchResponseDto;

  constructor(public authService: AuthService,
              public generalSearchService: GeneralSearchService,
              public router: Router) {
  }

  ngOnInit() {
    this.loadSearchItems();
  }

  loadSearchItems() {
    this.search$ = concat(of([]),
      this.searchInput$.pipe(debounceTime(500), distinctUntilChanged(), tap(() => this.searchLoading = true),
        switchMap(term =>
          this.generalSearchService.getAllMatching(term).pipe(catchError(() => of([])), tap(() => this.searchLoading = false)))));
  }

  redirect() {
    if (this.selectedSearchItem !== undefined) {
      this.router.navigate(['fails', this.selectedSearchItem.id]);
      /* if (this.selectedSearchItem.searchCategory === 'ARTISTS') {
        this.router.navigate(['fails', this.selectedSearchItem.id]);
      } */
    }
  }

  //groupByFn = (item: GeneralSearchResponseDto) => item.searchCategory;

}
