import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginRequest } from './login-request';
import { AuthService } from '../shared/auth.service';
import { LoginResponse } from './login-response';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  successSignUpMessage: string | undefined;
  isError: boolean | undefined;

  constructor(
    formBuilder: FormBuilder,
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private toastrService: ToastrService
  ) {
    this.loginForm = formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      if (params.registered && params.registered === 'true') {
        this.toastrService.success('Sign Up was successful!');
        this.successSignUpMessage =
          'Please check your E-Mail inbox for activation link for your account before logging in.';
      }
    });
  }

  login(): void {
    const username = this.loginForm.get('username')?.value;
    const password = this.loginForm.get('password')?.value;

    const request: LoginRequest = {
      username,
      password,
    };
    this.authService.login(request).subscribe(
      (response: LoginResponse) => {
        if (response) {
          this.isError = false;
          this.router.navigateByUrl('/');
          this.toastrService.success('Login was successful');
        }
      },
      (error: HttpErrorResponse) => {
        this.isError = true;
      }
    );
  }
}
