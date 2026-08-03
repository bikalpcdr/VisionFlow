import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { OverviewComponent } from './dashboard/overview/overview.component';
import { PatientsComponent } from './dashboard/patients/patients.component';
import { DoctorsComponent } from './dashboard/doctors/doctors.component';
import { AppointmentsComponent } from './dashboard/appointments/appointments.component';
import { AssessmentsComponent } from './dashboard/assessments/assessments.component';
import { TherapyPlansComponent } from './dashboard/therapy-plans/therapy-plans.component';
import { SessionsComponent } from './dashboard/sessions/sessions.component';
import { ReportsComponent } from './dashboard/reports/reports.component';
import { NotificationsComponent } from './dashboard/notifications/notifications.component';
import { authGuard } from './guards/auth-guard';
import { publicGuard } from './guards/public.guard';
import { adminGuard, doctorGuard } from './guards/role.guard';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent, canActivate: [publicGuard] },
    { path: 'register', component: RegisterComponent, canActivate: [publicGuard] },
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [authGuard],
        children: [
            { path: '',              redirectTo: 'overview', pathMatch: 'full' },
            { path: 'overview',      component: OverviewComponent },
            { path: 'patients',      component: PatientsComponent, canActivate: [doctorGuard] },
            { path: 'doctors',       component: DoctorsComponent, canActivate: [adminGuard] },
            { path: 'appointments',  component: AppointmentsComponent },
            { path: 'assessments',   component: AssessmentsComponent, canActivate: [doctorGuard] },
            { path: 'therapy-plans', component: TherapyPlansComponent, canActivate: [doctorGuard] },
            { path: 'sessions',      component: SessionsComponent },
            { path: 'reports',       component: ReportsComponent, canActivate: [doctorGuard] },
            { path: 'notifications', component: NotificationsComponent },
        ]
    },
    { path: '**', redirectTo: '' }
];
