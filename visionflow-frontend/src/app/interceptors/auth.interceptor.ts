import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const token = authService.getToken();
    if (token) {
        req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
    }

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            const urlPath = (() => {
                try {
                    return new URL(req.url).pathname;
                } catch {
                    return req.url;
                }
            })();
            const isAuthCall = urlPath.includes('/auth/');

            if (error.status === 401 && !isAuthCall) {
                return authService.refreshToken().pipe(
                    switchMap((response) => {
                        if (response.success) {
                            const retried = req.clone({
                                setHeaders: { Authorization: `Bearer ${authService.getToken()}` }
                            });
                            return next(retried);
                        }
                        authService.logout();
                        router.navigate(['/login']);
                        return throwError(() => error);
                    }),
                    catchError(() => {
                        authService.logout();
                        router.navigate(['/login']);
                        return throwError(() => error);
                    })
                );
            }

            return throwError(() => error);
        })
    );
};
