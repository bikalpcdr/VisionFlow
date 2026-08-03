import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../service/auth.service';

interface NavItem {
    label: string;
    icon: string;
    route: string;
    roles: string[];
}

const NAV_ITEMS: NavItem[] = [
    { label: 'Overview',      icon: '⊞',  route: '/dashboard/overview',      roles: ['ADMIN', 'DOCTOR', 'PATIENT'] },
    { label: 'Patients',      icon: '🧑',  route: '/dashboard/patients',      roles: ['ADMIN', 'DOCTOR'] },
    { label: 'Doctors',       icon: '👨‍⚕️', route: '/dashboard/doctors',       roles: ['ADMIN'] },
    { label: 'Appointments',  icon: '📅',  route: '/dashboard/appointments',  roles: ['ADMIN', 'DOCTOR', 'PATIENT'] },
    { label: 'Assessments',   icon: '👁',  route: '/dashboard/assessments',   roles: ['ADMIN', 'DOCTOR', 'PATIENT'] },
    { label: 'Therapy Plans', icon: '📋',  route: '/dashboard/therapy-plans', roles: ['ADMIN', 'DOCTOR', 'PATIENT'] },
    { label: 'Sessions',      icon: '🏃',  route: '/dashboard/sessions',      roles: ['ADMIN', 'DOCTOR', 'PATIENT'] },
    { label: 'Reports',       icon: '📊',  route: '/dashboard/reports',       roles: ['ADMIN', 'DOCTOR'] },
    { label: 'Notifications', icon: '🔔',  route: '/dashboard/notifications', roles: ['ADMIN', 'DOCTOR', 'PATIENT'] },
];

@Component({
    selector: 'app-sidebar',
    standalone: true,
    imports: [CommonModule, RouterLink, RouterLinkActive],
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
    private authService = inject(AuthService);
    collapsed = signal(false);

    get navItems(): NavItem[] {
        const role = this.authService.getUser()?.role ?? '';
        return NAV_ITEMS.filter(item => item.roles.includes(role));
    }

    toggle() {
        this.collapsed.update(v => !v);
    }
}
