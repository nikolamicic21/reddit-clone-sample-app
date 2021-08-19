import { Component, OnInit } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { SubRedditResponseModel } from '../sub-reddit-response.model';
import { SubRedditService } from '../sub-reddit.service';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-list-sub-reddit',
  templateUrl: './list-sub-reddit.component.html',
  styleUrls: ['./list-sub-reddit.component.scss'],
})
export class ListSubRedditComponent implements OnInit {
  subReddits$: Observable<SubRedditResponseModel[]> | undefined;
  constructor(private subRedditService: SubRedditService) {}

  ngOnInit(): void {
    this.subReddits$ = this.subRedditService
      .getAll()
      .pipe(catchError((err) => throwError(err)));
  }
}
