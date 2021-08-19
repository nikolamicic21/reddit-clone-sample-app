import { Component, OnInit } from '@angular/core';
import { PostResponseModel } from '../shared/post-response.model';
import { Observable, throwError } from 'rxjs';
import { PostService } from '../shared/post.service';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  posts$: Observable<PostResponseModel[]>;
  constructor(private postService: PostService) {
    this.posts$ = this.postService
      .getAll()
      .pipe(catchError((error) => throwError(error)));
  }

  ngOnInit(): void {}
}
