import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

const ROLE_HIERARCHY: Record<string, string[]> = {
  ADMIN: ['ADMIN'],
  DOCTOR: ['ADMIN', 'DOCTOR'],
  PATIENT: ['ADMIN', 'DOCTOR', 'PATIENT'],
};

export const roleGuard =
  (allowedRoles: string[]): CanActivateFn =>
  (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const user = authService.getUser();

    if (!user) {
      router.navigate(['/login']);
      return false;
    }

    const hasRole = allowedRoles.includes(user.role);
    if (!hasRole) {
      router.navigate(['/dashboard']);
      return false;
    }

    return true;
  };

export const adminGuard = roleGuard(ROLE_HIERARCHY['ADMIN']);
export const doctorGuard = roleGuard(ROLE_HIERARCHY['DOCTOR']);
export const patientGuard = roleGuard(ROLE_HIERARCHY['PATIENT']);
