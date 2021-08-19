import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/shared/auth.service';
import { BehaviorSubject, Subject } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  isLoggedIn: BehaviorSubject<boolean>;
  username: BehaviorSubject<string | undefined>;

  constructor(private authService: AuthService, private router: Router) {
    this.isLoggedIn = this.authService.isLoggedIn;
    this.username = this.authService.username;
  }

  ngOnInit(): void {}

  goToUserProfile(): void {
    this.router.navigateByUrl(`/user-profile/${this.username.value}`);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/');
  }
}
