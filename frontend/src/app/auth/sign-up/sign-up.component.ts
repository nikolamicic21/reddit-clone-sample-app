import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../shared/auth.service';
import { SignUpRequest } from './sign-up-request';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss'],
})
export class SignUpComponent implements OnInit {
  signUpForm: FormGroup;

  constructor(
    formBuilder: FormBuilder,
    private authService: AuthService,
    private toastrService: ToastrService,
    private router: Router
  ) {
    this.signUpForm = formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {}

  signUp(): void {
    const email = this.signUpForm.get('email')?.value;
    const username = this.signUpForm.get('username')?.value;
    const password = this.signUpForm.get('password')?.value;

    const signUpRequest: SignUpRequest = {
      email,
      username,
      password,
    };
    this.authService.signUp(signUpRequest).subscribe(
      (response: any) => {
        this.router.navigate(['/login'], { queryParams: { registered: 'true' } });
      },
      (error: HttpErrorResponse) => {
        this.toastrService.error('Registration failed! Please try again!');
      }
    );
  }
}
