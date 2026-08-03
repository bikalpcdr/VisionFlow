import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css'],
})
export class OverviewComponent {
  private authService = inject(AuthService);
  user = this.authService.getUser();

  get greeting(): string {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  }

  // Stat cards per role
  get stats() {
    if (this.user?.role === 'ADMIN')
      return [
        {
          label: 'Total Patients',
          value: '—',
          icon: '🧑',
          route: '/dashboard/patients',
          color: 'blue',
        },
        {
          label: 'Total Doctors',
          value: '—',
          icon: '👨⚕️',
          route: '/dashboard/doctors',
          color: 'purple',
        },
        {
          label: 'Appointments Today',
          value: '—',
          icon: '📅',
          route: '/dashboard/appointments',
          color: 'green',
        },
        {
          label: 'Active Plans',
          value: '—',
          icon: '📋',
          route: '/dashboard/therapy-plans',
          color: 'orange',
        },
      ];
    if (this.user?.role === 'DOCTOR')
      return [
        {
          label: 'My Patients',
          value: '—',
          icon: '🧑',
          route: '/dashboard/patients',
          color: 'blue',
        },
        {
          label: 'Appointments Today',
          value: '—',
          icon: '📅',
          route: '/dashboard/appointments',
          color: 'green',
        },
        {
          label: 'Active Plans',
          value: '—',
          icon: '📋',
          route: '/dashboard/therapy-plans',
          color: 'purple',
        },
        {
          label: 'Pending Sessions',
          value: '—',
          icon: '🏃',
          route: '/dashboard/sessions',
          color: 'orange',
        },
      ];
    return [
      {
        label: 'My Appointments',
        value: '—',
        icon: '📅',
        route: '/dashboard/appointments',
        color: 'blue',
      },
      {
        label: 'My Sessions',
        value: '—',
        icon: '🏃',
        route: '/dashboard/sessions',
        color: 'green',
      },
      {
        label: 'My Therapy Plan',
        value: '—',
        icon: '📋',
        route: '/dashboard/therapy-plans',
        color: 'purple',
      },
      {
        label: 'My Assessments',
        value: '—',
        icon: '👁',
        route: '/dashboard/assessments',
        color: 'orange',
      },
    ];
  }

  quickLinks = [
    { label: 'Appointments', icon: '📅', route: '/dashboard/appointments' },
    { label: 'Assessments', icon: '👁', route: '/dashboard/assessments' },
    { label: 'Therapy Plans', icon: '📋', route: '/dashboard/therapy-plans' },
    { label: 'Sessions', icon: '🏃', route: '/dashboard/sessions' },
    { label: 'Notifications', icon: '🔔', route: '/dashboard/notifications' },
    { label: 'Reports', icon: '📊', route: '/dashboard/reports' },
  ];
}
