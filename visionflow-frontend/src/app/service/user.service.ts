import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/configs/api.config';
import { ApiResponse } from '../shared/models/api-response';

export interface UserProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  role: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private http = inject(HttpClient);
  private base = API_CONFIG.BASE_URL;

  // GET /users  — ADMIN, DOCTOR
  getAll(): Observable<ApiResponse<UserProfile[]>> {
    return this.http.get<ApiResponse<UserProfile[]>>(
      `${this.base}${API_CONFIG.ENDPOINTS.USERS.BASE}`,
    );
  }

  // GET /users/{id}  — ADMIN, DOCTOR, self
  getById(id: number): Observable<ApiResponse<UserProfile>> {
    return this.http.get<ApiResponse<UserProfile>>(
      `${this.base}${API_CONFIG.ENDPOINTS.USERS.BY_ID(id)}`,
    );
  }

  // PATCH /users/{id}/deactivate  — ADMIN
  deactivate(id: number): Observable<ApiResponse<void>> {
    return this.http.patch<ApiResponse<void>>(
      `${this.base}${API_CONFIG.ENDPOINTS.USERS.DEACTIVATE(id)}`,
      {},
    );
  }
}
