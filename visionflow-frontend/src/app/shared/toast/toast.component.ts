import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from './toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      @for (toast of toastService.toasts(); track toast.id) {
        <div class="toast toast-{{ toast.type }}">
          <span>{{ toast.message }}</span>
          <button (click)="toastService.dismiss(toast.id)">✕</button>
        </div>
      }
    </div>
  `,
  styleUrls: ['./toast.component.css'],
})
export class ToastComponent {
  toastService = inject(ToastService);
}
