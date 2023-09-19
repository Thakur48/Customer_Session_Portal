import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/authentication-service/auth.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  constructor(public service: AuthService, private route: Router) {}

  logOut() {
    this.service.logout();
    localStorage.clear();
    this.route.navigateByUrl('/login');
  }
}
