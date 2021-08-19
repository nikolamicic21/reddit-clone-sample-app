import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { LoginResponse } from '../login/login-response';

@Injectable({
  providedIn: 'root',
})
export class TokenInterceptor implements HttpInterceptor {
  private isTokenRefreshing = false;
  private tokenSubject = new BehaviorSubject<string | null>(null);

  constructor(private authService: AuthService) {}

  private static addToken(
    request: HttpRequest<any>,
    token: string
  ): HttpRequest<any> {
    return request.clone({
      headers: request.headers.set('Authorization', `Bearer ${token}`),
    });
  }

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (
      request.url.indexOf('refresh') !== -1 ||
      request.url.indexOf('login') !== -1
    ) {
      return next.handle(request);
    }

    const token = this.authService.getToken();
    let clonedRequest = request.clone();
    if (token) {
      clonedRequest = TokenInterceptor.addToken(clonedRequest, token);
    }

    return next.handle(clonedRequest).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse && error.status === 403) {
          return this.handleNotAuthorizedError(clonedRequest, next);
        } else {
          this.authService.isLoggedIn.next(false);
          this.authService.username.next(undefined);
          return throwError(error);
        }
      })
    );
  }

  private handleNotAuthorizedError(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.tokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((response: LoginResponse) => {
          this.isTokenRefreshing = false;
          this.tokenSubject.next(response.token);
          this.authService.isLoggedIn.next(true);
          this.authService.username.next(response.username);

          return next.handle(
            TokenInterceptor.addToken(request, response.token)
          );
        })
      );
    } else {
      return this.tokenSubject.pipe(
        filter((result) => result !== null),
        take(1),
        switchMap(() => {
          return next.handle(
            TokenInterceptor.addToken(
              request,
              this.authService.getToken() as string
            )
          );
        })
      );
    }
  }
}
