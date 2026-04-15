import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth-service';
import { inject } from '@angular/core';
import { catchError, from, switchMap, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  return next(req).pipe(
      catchError((error: HttpErrorResponse) => {

        if (error.status === 401) {
          return from(authService.refreshToken()).pipe(
            switchMap(() => {
              return next(req);
            }),
            catchError(() => {
              authService.logout();
              return throwError(() => error);
            })
          );

        }

        return throwError(() => error);
      })
    );
};
