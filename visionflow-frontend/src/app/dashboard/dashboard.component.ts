import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { ToastService } from '../shared/toast/toast.service';
import { SidebarComponent } from './sidebar/sidebar.component';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [RouterOutlet, RouterLink, SidebarComponent],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
    private authService = inject(AuthService);
    private toast = inject(ToastService);
    router = inject(Router);

    get user() { return this.authService.getUser(); }

    get pageTitle(): string {
        const segment = this.router.url.split('/').pop() ?? 'overview';
        return segment.replace(/-/g, ' ').replace(/\b\w/g, c => c.toUpperCase());
    }

    logout() {
        this.authService.logout();
        this.toast.success('Logged out successfully.');
        this.router.navigate(['/login']);
    }
}
