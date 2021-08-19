import { Component, OnInit } from '@angular/core';
import { SubRedditService } from '../../sub-reddit/sub-reddit.service';
import { BehaviorSubject, combineLatest, Observable } from 'rxjs';
import { SubRedditResponseModel } from '../../sub-reddit/sub-reddit-response.model';
import { map, take, tap } from 'rxjs/operators';

@Component({
  selector: 'app-subreddit-side-bar',
  templateUrl: './subreddit-side-bar.component.html',
  styleUrls: ['./subreddit-side-bar.component.scss'],
})
export class SubredditSideBarComponent implements OnInit {
  subReddits$: Observable<SubRedditResponseModel[]> | undefined;
  displayAll$ = new BehaviorSubject<boolean>(false);

  constructor(private subRedditService: SubRedditService) {}

  ngOnInit(): void {
    this.subReddits$ = combineLatest([
      this.subRedditService.getAll(),
      this.displayAll$,
    ]).pipe(
      map((it: [SubRedditResponseModel[], boolean]) => {
        if (!it[1] && it[0].length > 4) {
          return it[0].slice(0, 3);
        } else {
          return it[0];
        }
      })
    );
  }
}
