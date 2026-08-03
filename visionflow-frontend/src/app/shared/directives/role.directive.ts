import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../../service/auth.service';

@Directive({
  selector: '[appHasRole]',
  standalone: true,
})
export class HasRoleDirective {
  @Input('appHasRole') set allowedRoles(roles: string[]) {
    const user = this.authService.getUser();
    const hasAccess = user ? roles.includes(user.role) : false;
    this.vcRef.clear();
    if (hasAccess) {
      this.vcRef.createEmbeddedView(this.templateRef);
    }
  }

  constructor(
    private templateRef: TemplateRef<any>,
    private vcRef: ViewContainerRef,
    private authService: AuthService
  ) {}
}
