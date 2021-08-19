import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, throwError } from 'rxjs';
import { SubRedditResponseModel } from '../../sub-reddit/sub-reddit-response.model';
import { Router } from '@angular/router';
import { SubRedditService } from '../../sub-reddit/sub-reddit.service';
import { catchError } from 'rxjs/operators';
import { CreatePostRequestModel } from './create-post-request.model';
import { PostService } from '../../shared/post.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss'],
})
export class CreatePostComponent implements OnInit {
  createPostForm: FormGroup;
  subReddits$: Observable<SubRedditResponseModel[]> | undefined;

  constructor(
    formBuilder: FormBuilder,
    private router: Router,
    private subRedditService: SubRedditService,
    private postService: PostService
  ) {
    this.createPostForm = formBuilder.group({
      name: ['', Validators.required],
      url: ['', Validators.required],
      subRedditName: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.subReddits$ = this.subRedditService
      .getAll()
      .pipe(catchError((error) => throwError(error)));
  }

  createPost(): void {
    const request: CreatePostRequestModel = {
      name: this.createPostForm.get('name')?.value,
      url: this.createPostForm.get('url')?.value,
      subRedditName: this.createPostForm.get('subRedditName')?.value,
      description: this.createPostForm.get('description')?.value,
    };
    this.postService.create(request).subscribe(
      (response) => {
        this.router.navigateByUrl('/');
      },
      (error) => throwError(error)
    );
  }

  discardPost(): void {
    this.router.navigateByUrl('/');
  }
}
