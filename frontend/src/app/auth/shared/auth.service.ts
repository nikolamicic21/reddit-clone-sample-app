import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SignUpRequest } from '../sign-up/sign-up-request';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { LoginRequest } from '../login/login-request';
import { LoginResponse } from '../login/login-response';
import { catchError, map, tap } from 'rxjs/operators';
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  isLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  username: BehaviorSubject<string | undefined> = new BehaviorSubject<
    string | undefined
  >(undefined);
  constructor(
    private httpClient: HttpClient,
    private localStorage: LocalStorageService
  ) {}

  signUp(signUpRequest: SignUpRequest): Observable<any> {
    return this.httpClient.post<any>(
      'http://localhost:8080/api/auth/sign-up',
      signUpRequest
    );
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.httpClient
      .post<LoginResponse>('http://localhost:8080/api/auth/login', loginRequest)
      .pipe(
        tap((response: LoginResponse) => {
          this.localStorage.store('username', response.username);
          this.localStorage.store('token', response.token);
          this.localStorage.store('refreshToken', response.refreshToken);
          this.localStorage.store('expiresAt', response.expiresAt);
          this.isLoggedIn.next(true);
          this.username.next(response.username);
        }),
        catchError((error) => {
          this.isLoggedIn.next(false);
          this.username.next(undefined);
          return throwError(error);
        })
      );
  }

  getToken(): string | undefined {
    return this.localStorage.retrieve('token');
  }

  refreshToken(): Observable<LoginResponse> {
    const request = {
      refreshToken: this.getRefreshToken(),
      username: this.getUsername(),
    };
    return this.httpClient
      .post<LoginResponse>(
        'http://localhost:8080/api/auth/token/refresh',
        request
      )
      .pipe(
        tap((response) => {
          this.localStorage.store('token', response.token);
          this.localStorage.store('expiresAt', response.expiresAt);
          this.isLoggedIn.next(true);
          this.username.next(response.username);
        }),
        catchError((error) => {
          this.isLoggedIn.next(false);
          this.username.next(undefined);
          return throwError(error);
        })
      );
  }

  private getRefreshToken(): string | undefined {
    return this.localStorage.retrieve('refreshToken');
  }

  private getUsername(): string | undefined {
    return this.localStorage.retrieve('username');
  }

  logout(): void {
    this.httpClient
      .post('http://localhost:8080/api/auth/logout', this.getRefreshToken())
      .subscribe(
        (response) => {
          console.log(response);
        },
        (error) => throwError(error)
      );
    this.localStorage.clear('username');
    this.localStorage.clear('token');
    this.localStorage.clear('refreshToken');
    this.localStorage.clear('expiresAt');
  }
}
