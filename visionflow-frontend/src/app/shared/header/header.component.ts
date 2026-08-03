import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthData, AuthService } from '../../service/auth.service';
import { ToastService } from '../toast/toast.service';

@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private toast = inject(ToastService);

  get user(): AuthData | null {
    return this.authService.getUser();
  }

  logout() {
    this.authService.logout();
    this.toast.success('Logged out successfully.');
    this.router.navigate(['/login']);
  }
}
