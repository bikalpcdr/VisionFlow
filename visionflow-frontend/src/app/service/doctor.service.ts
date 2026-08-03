import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../shared/configs/api.config';
import { ApiResponse } from '../shared/models/api-response';

export interface Doctor {
    id: number;
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    specialization: string;
    licenseNumber: string;
    active: boolean;
    createdAt: string;
    updatedAt: string;
}

export interface CreateDoctorRequest {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    phone: string;
    specialization: string;
    licenseNumber: string;
}

export interface UpdateDoctorRequest {
    firstName: string;
    lastName: string;
    phone: string;
    specialization: string;
    licenseNumber: string;
}

export interface DoctorFilterParams {
    specialization?: string;
    active?: boolean;
}

@Injectable({ providedIn: 'root' })
export class DoctorService {
    private http = inject(HttpClient);
    private base = API_CONFIG.BASE_URL;

    // GET /doctors — ADMIN, DOCTOR
    getAll(filters?: DoctorFilterParams): Observable<ApiResponse<Doctor[]>> {
        let params = new HttpParams();
        if (filters?.specialization) params = params.set('specialization', filters.specialization);
        if (filters?.active !== undefined) params = params.set('active', String(filters.active));
        return this.http.get<ApiResponse<Doctor[]>>(
            `${this.base}${API_CONFIG.ENDPOINTS.DOCTORS.BASE}`, { params }
        );
    }

    // GET /doctors/{id} — ADMIN, DOCTOR
    getById(id: number): Observable<ApiResponse<Doctor>> {
        return this.http.get<ApiResponse<Doctor>>(
            `${this.base}${API_CONFIG.ENDPOINTS.DOCTORS.BY_ID(id)}`
        );
    }

    // POST /doctors — ADMIN
    create(payload: CreateDoctorRequest): Observable<ApiResponse<Doctor>> {
        return this.http.post<ApiResponse<Doctor>>(
            `${this.base}${API_CONFIG.ENDPOINTS.DOCTORS.BASE}`, payload
        );
    }

    // PUT /doctors/{id} — ADMIN, owner DOCTOR
    update(id: number, payload: UpdateDoctorRequest): Observable<ApiResponse<Doctor>> {
        return this.http.put<ApiResponse<Doctor>>(
            `${this.base}${API_CONFIG.ENDPOINTS.DOCTORS.BY_ID(id)}`, payload
        );
    }

    // PATCH /doctors/{id}/deactivate — ADMIN
    deactivate(id: number): Observable<ApiResponse<void>> {
        return this.http.patch<ApiResponse<void>>(
            `${this.base}${API_CONFIG.ENDPOINTS.DOCTORS.DEACTIVATE(id)}`, {}
        );
    }
}
