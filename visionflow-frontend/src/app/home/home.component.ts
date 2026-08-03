import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  get isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }

  features = [
    {
      icon: '👁',
      title: 'Vision Assessments',
      description:
        'Comprehensive visual acuity, refraction, binocular vision, and IOP assessments in one place.',
    },
    {
      icon: '📋',
      title: 'Therapy Plans',
      description:
        'Create and manage personalized therapy plans with structured exercises for each patient.',
    },
    {
      icon: '📅',
      title: 'Appointments',
      description: 'Smart scheduling with conflict detection — no overlapping doctor slots, ever.',
    },
    {
      icon: '📊',
      title: 'Progress Reports',
      description:
        'Track patient progress, doctor workload, and clinic-wide performance at a glance.',
    },
    {
      icon: '🔔',
      title: 'Notifications',
      description:
        'Real-time in-app notifications for appointments, sessions, and therapy plan updates.',
    },
    {
      icon: '🔒',
      title: 'Role-Based Access',
      description:
        'Secure JWT authentication with fine-grained access control for Admins, Doctors, and Patients.',
    },
  ];

  year = new Date().getFullYear();
}
