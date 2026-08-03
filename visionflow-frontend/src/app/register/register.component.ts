import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService, RegisterRequest } from '../service/auth.service';
import { ToastService } from '../shared/toast/toast.service';
import { finalize } from 'rxjs';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [FormsModule, RouterLink],
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
    payload: RegisterRequest = {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        phone: '',
        role: 'PATIENT'
    };

    isLoading = false;
    showPassword = false;

    constructor(
        private authService: AuthService,
        private router: Router,
        private toast: ToastService
    ) {}

    ngOnInit() {
        if (this.authService.isLoggedIn()) {
            this.router.navigate(['/dashboard']);
        }
    }

    onSubmit() {
        this.isLoading = true;

        this.authService.register(this.payload).pipe(
            finalize(() => this.isLoading = false)
        ).subscribe({
            next: (response) => {
                if (response.success) {
                    this.toast.success(response.message);
                    this.router.navigate(['/dashboard']);
                } else {
                    this.toast.error(response.message || 'Registration failed. Please try again.');
                }
            },
            error: (error) => {
                const message = error?.error?.message || error?.message || 'Cannot connect to server.';
                this.toast.error(message);
            }
        });
    }
}
