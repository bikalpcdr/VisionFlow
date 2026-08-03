import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DoctorService, Doctor, CreateDoctorRequest, UpdateDoctorRequest } from '../../service/doctor.service';
import { AuthService } from '../../service/auth.service';
import { ToastService } from '../../shared/toast/toast.service';

@Component({
    selector: 'app-doctors',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './doctors.component.html',
    styleUrls: ['./doctors.component.css']
})
export class DoctorsComponent implements OnInit {
    private doctorService = inject(DoctorService);
    private authService = inject(AuthService);
    private toast = inject(ToastService);

    doctors: Doctor[] = [];
    filtered: Doctor[] = [];
    isLoading = false;

    searchQuery = '';
    filterActive: boolean | undefined = undefined;
    filterSpecialization = '';

    showModal = false;
    isEditMode = false;
    isSaving = false;
    selectedDoctor: Doctor | null = null;

    showDeactivateConfirm = false;
    deactivateTarget: Doctor | null = null;

    showDetail = false;
    detailDoctor: Doctor | null = null;

    formErrors: Partial<Record<string, string>> = {};

    get isAdmin() { return this.authService.getUser()?.role === 'ADMIN'; }
    get currentUserId() { return this.authService.getUser()?.id; }
    get isDoctor() { return this.authService.getUser()?.role === 'DOCTOR'; }

    createForm: CreateDoctorRequest = this.emptyCreateForm();
    editForm: UpdateDoctorRequest = this.emptyEditForm();

    ngOnInit() { this.load(); }

    load() {
        this.isLoading = true;
        const filters = {
            specialization: this.filterSpecialization || undefined,
            active: this.filterActive
        };
        this.doctorService.getAll(filters).subscribe({
            next: (res) => {
                this.isLoading = false;
                if (res.success) {
                    this.doctors = res.data;
                    this.applyFilter();
                }
            },
            error: (err) => {
                this.isLoading = false;
                this.toast.error(err?.error?.message || 'Failed to load doctors.');
            }
        });
    }

    applyFilter() {
        this.filtered = this.doctors.filter(d => {
            const q = this.searchQuery.toLowerCase();
            const matchSearch = !q ||
                d.firstName.toLowerCase().includes(q) ||
                d.lastName.toLowerCase().includes(q) ||
                d.email.toLowerCase().includes(q) ||
                d.specialization.toLowerCase().includes(q);
            const matchActive = this.filterActive === undefined || d.active === this.filterActive;
            const matchSpec = !this.filterSpecialization ||
                d.specialization.toLowerCase().includes(this.filterSpecialization.toLowerCase());
            return matchSearch && matchActive && matchSpec;
        });
    }

    openCreate() {
        this.isEditMode = false;
        this.createForm = this.emptyCreateForm();
        this.showModal = true;
    }

    openEdit(doctor: Doctor) {
        this.isEditMode = true;
        this.selectedDoctor = doctor;
        this.editForm = {
            firstName: doctor.firstName,
            lastName: doctor.lastName,
            phone: doctor.phone,
            specialization: doctor.specialization,
            licenseNumber: doctor.licenseNumber
        };
        this.formErrors = {};
        this.showModal = true;
    }

    canEdit(doctor: Doctor): boolean {
        if (this.isAdmin) return true;
        // owner DOCTOR can edit their own profile
        if (this.isDoctor && doctor.userId === this.currentUserId) return true;
        return false;
    }

    openDetail(doctor: Doctor) {
        this.detailDoctor = doctor;
        this.showDetail = true;
    }

    closeDetail() {
        this.showDetail = false;
        this.detailDoctor = null;
    }

    closeModal() {
        this.showModal = false;
        this.selectedDoctor = null;
        this.formErrors = {};
    }

    validate(): boolean {
        this.formErrors = {};
        if (this.isEditMode) {
            if (!this.editForm.firstName.trim()) this.formErrors['firstName'] = 'Required';
            if (!this.editForm.lastName.trim())  this.formErrors['lastName']  = 'Required';
            if (!this.editForm.phone.trim())      this.formErrors['phone']     = 'Required';
            if (!this.editForm.specialization.trim()) this.formErrors['specialization'] = 'Required';
            if (!this.editForm.licenseNumber.trim())  this.formErrors['licenseNumber']  = 'Required';
        } else {
            if (!this.createForm.firstName.trim()) this.formErrors['firstName'] = 'Required';
            if (!this.createForm.lastName.trim())  this.formErrors['lastName']  = 'Required';
            if (!this.createForm.email.trim())     this.formErrors['email']     = 'Required';
            if (!this.createForm.password.trim())  this.formErrors['password']  = 'Required';
            if (!this.createForm.phone.trim())     this.formErrors['phone']     = 'Required';
            if (!this.createForm.specialization.trim()) this.formErrors['specialization'] = 'Required';
            if (!this.createForm.licenseNumber.trim())  this.formErrors['licenseNumber']  = 'Required';
        }
        return Object.keys(this.formErrors).length === 0;
    }

    save() {
        if (!this.validate()) return;
        this.isSaving = true;
        const call = this.isEditMode
            ? this.doctorService.update(this.selectedDoctor!.id, this.editForm)
            : this.doctorService.create(this.createForm);

        call.subscribe({
            next: (res) => {
                this.isSaving = false;
                this.toast.success(res.message);
                this.closeModal();
                this.load();
            },
            error: (err) => {
                this.isSaving = false;
                this.toast.error(err?.error?.message || 'Operation failed.');
            }
        });
    }

    confirmDeactivate(doctor: Doctor) {
        this.deactivateTarget = doctor;
        this.showDeactivateConfirm = true;
    }

    deactivate() {
        if (!this.deactivateTarget) return;
        this.doctorService.deactivate(this.deactivateTarget.id).subscribe({
            next: (res) => {
                this.toast.success(res.message);
                this.showDeactivateConfirm = false;
                this.deactivateTarget = null;
                this.load();
            },
            error: (err) => {
                this.toast.error(err?.error?.message || 'Deactivation failed.');
            }
        });
    }

    get specializations(): string[] {
        return [...new Set(this.doctors.map(d => d.specialization).filter(Boolean))];
    }

    private emptyCreateForm(): CreateDoctorRequest {
        return { firstName: '', lastName: '', email: '', password: '', phone: '', specialization: '', licenseNumber: '' };
    }

    private emptyEditForm(): UpdateDoctorRequest {
        return { firstName: '', lastName: '', phone: '', specialization: '', licenseNumber: '' };
    }
}
