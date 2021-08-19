import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { PostService } from '../../shared/post.service';
import { CommentService } from '../../comment/comment.service';
import { Observable } from 'rxjs';
import { PostResponseModel } from '../../shared/post-response.model';
import { CommentResponseModel } from '../../comment/comment-response.model';
import { map, tap, withLatestFrom } from 'rxjs/operators';
import { UserProfileModel } from './user-profile.model';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
})
export class UserProfileComponent implements OnInit {
  userProfileModel$: Observable<UserProfileModel> | undefined;
  username: string | undefined;

  constructor(
    private activatedRoute: ActivatedRoute,
    private postService: PostService,
    private commentService: CommentService
  ) {}

  ngOnInit(): void {
    this.username = this.activatedRoute.snapshot.params.name as string;
    this.userProfileModel$ = this.postService.getByUserName(this.username).pipe(
      withLatestFrom(this.commentService.getByUserName(this.username)),
      tap(([posts, comments]) => {
        console.log(posts.length);
        console.log(comments.length);
      }),
      map(([posts, comments]) => ({
        posts,
        comments,
      }))
    );
  }
}
