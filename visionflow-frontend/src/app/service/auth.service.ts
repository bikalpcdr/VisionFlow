import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_CONFIG } from '../shared/configs/api.config';
import { ApiResponse } from '../shared/models/api-response';
import { UserProfile } from './user.service';

export interface AuthData {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
  role: 'DOCTOR' | 'PATIENT';
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = API_CONFIG.BASE_URL;

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<ApiResponse<AuthData>> {
    return this.http
      .post<ApiResponse<AuthData>>(`${this.baseUrl}${API_CONFIG.ENDPOINTS.AUTH.LOGIN}`, credentials)
      .pipe(
        tap((response) => {
          if (response.success) {
            this.storeSession(response.data);
          }
        }),
      );
  }

  register(payload: RegisterRequest): Observable<ApiResponse<AuthData>> {
    return this.http
      .post<ApiResponse<AuthData>>(`${this.baseUrl}${API_CONFIG.ENDPOINTS.AUTH.REGISTER}`, payload)
      .pipe(
        tap((response) => {
          if (response.success) {
            this.storeSession(response.data);
          }
        }),
      );
  }

  refreshToken(): Observable<ApiResponse<AuthData>> {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http
      .post<ApiResponse<AuthData>>(`${this.baseUrl}${API_CONFIG.ENDPOINTS.AUTH.REFRESH}`, {
        refreshToken,
      })
      .pipe(
        tap((response) => {
          if (response.success) {
            this.storeSession(response.data);
          }
        }),
      );
  }

  getCurrentUserProfile(): Observable<ApiResponse<UserProfile>> {
    const user = this.getUser();
    if (!user) {
      throw new Error('No user logged in');
    }
    return this.http.get<ApiResponse<UserProfile>>(
      `${this.baseUrl}${API_CONFIG.ENDPOINTS.USERS.BY_ID(user.id)}`
    );
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  }

  getUser(): AuthData | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  hasRole(role: string): boolean {
    const user = this.getUser();
    return user ? user.role === role : false;
  }

  private storeSession(data: AuthData): void {
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);
    localStorage.setItem('user', JSON.stringify(data));
  }
}
