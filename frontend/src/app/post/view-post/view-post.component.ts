import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { PostResponseModel } from '../../shared/post-response.model';
import { PostService } from '../../shared/post.service';
import { catchError } from 'rxjs/operators';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CreateCommentRequestModel } from '../../comment/create-comment-request.model';
import { CommentService } from '../../comment/comment.service';
import { CommentResponseModel } from '../../comment/comment-response.model';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.scss'],
})
export class ViewPostComponent implements OnInit {
  post$: Observable<PostResponseModel> | undefined;
  commentForm: FormGroup;
  comments$: Observable<CommentResponseModel[]> | undefined;
  private postName: string | undefined;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private postService: PostService,
    private commentService: CommentService
  ) {
    this.commentForm = this.formBuilder.group({
      text: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      this.postName = params.name as string;
      this.getPost();
      this.getCommentsForPost();
    });
  }

  postComment(): void {
    const request: CreateCommentRequestModel = {
      text: this.commentForm.get('text')?.value,
      postName: this.postName as string,
    };
    this.commentService.create(request).subscribe(
      (response) => {
        this.commentForm.get('text')?.setValue('');
        this.getCommentsForPost();
      },
      (error) => {
        throwError(error);
      }
    );
  }

  private getPost(): void {
    this.post$ = this.postService
      .getByName(this.postName as string)
      .pipe(catchError((error) => throwError(error)));
  }

  private getCommentsForPost(): void {
    this.comments$ = this.commentService
      .getByPostName(this.postName as string)
      .pipe(catchError((error) => throwError(error)));
  }
}
