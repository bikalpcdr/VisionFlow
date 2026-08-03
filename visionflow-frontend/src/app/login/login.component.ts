import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, LoginRequest } from '../service/auth.service';
import { ToastService } from '../shared/toast/toast.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  credentials: LoginRequest = { email: '', password: '' };
  isLoading = false;
  showPassword = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private toast: ToastService,
  ) {}

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  onSubmit() {
    if (this.isLoading) return;

    this.authService
      .login(this.credentials)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.toast.success(response.message);
            this.router.navigate(['/dashboard']);
          } else {
            this.toast.error(response.message || 'Login failed. Please try again.');
          }
        },
        error: (error) => {
          this.isLoading = false;
          const message = error?.error?.message || error?.message || 'Cannot connect to server.';
          this.toast.error(message);
        },
      });
  }
}
