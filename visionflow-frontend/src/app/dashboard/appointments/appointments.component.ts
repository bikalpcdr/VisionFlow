import { Component } from '@angular/core';

@Component({
    selector: 'app-appointments',
    standalone: true,
    template: `
        <div class="placeholder-page">
            <h2>Appointments</h2>
            <p>This module is coming soon.</p>
        </div>
    `,
    styles: [`
      .placeholder-page {
        background: white;
        border-radius: 14px;
        padding: 3rem;
        border: 1px solid #e2e8f0;
        text-align: center;
        color: #64748b;
      }

      h2 {
        font-size: 1.4rem;
        font-weight: 700;
        color: #0f172a;
        margin-bottom: 0.5rem;
      }
    `]
})
export class AppointmentsComponent {}
