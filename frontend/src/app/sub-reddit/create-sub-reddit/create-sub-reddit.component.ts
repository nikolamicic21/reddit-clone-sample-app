import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SubRedditService } from '../sub-reddit.service';
import { CreateSubRedditRequestModel } from './create-sub-reddit-request.model';
import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-create-sub-reddit',
  templateUrl: './create-sub-reddit.component.html',
  styleUrls: ['./create-sub-reddit.component.scss'],
})
export class CreateSubRedditComponent implements OnInit {
  createSubRedditForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private subRedditService: SubRedditService
  ) {
    this.createSubRedditForm = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
    });
  }

  ngOnInit(): void {}

  createSubReddit(): void {
    const request: CreateSubRedditRequestModel = {
      name: this.createSubRedditForm.get('title')?.value,
      description: this.createSubRedditForm.get('description')?.value,
    };
    this.subRedditService.create(request).subscribe(
      (response) => {
        this.router.navigateByUrl('/list-subreddit');
      },
      (error: HttpErrorResponse) => {
        throwError(error);
      }
    );
  }

  discard(): void {
    this.router.navigateByUrl('/');
  }
}
